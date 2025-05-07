from urllib.parse import urlparse
import itertools
import mechanicalsoup
from collections import defaultdict
from mechanicalsoup.stateful_browser import StatefulBrowser
from src.utils import info, warn, debug, create_section_header, format_input, Colors, normalize_url, progress_bar
from src.auth import authenticate

class URL:
  raw_url: str
  url: str
  parsed_url: str
  inputs: dict[str, str]

  def __init__(self, raw_url: str, base_url: str, parsed_url: str, inputs: dict[str, str]):
    self.raw_url = raw_url
    self.url = base_url
    self.parsed_url = parsed_url
    self.inputs = inputs

  def has_inputs(self):
    return len(self.inputs) != 0

  def with_query(self, key: str, value: str):
    return f"{self.parsed_url}?{key}={value}"

  @classmethod
  def fix_url(cls, url: str, relative_base_url: str = None, base_url: str = None):
    """
    fixes/normalizes a link
    relative_base_url is the url of the current page used for ../ traversal
    base_url the home URL eused for ./ traversal
    """
    if not url:
      return url

    # URLs with just: ?key=value
    if url.startswith('?'):
      if relative_base_url:
        base_without_query = relative_base_url.split('?')[0]
        return f"{base_without_query}{url}"
      return url

    # seperate query from url
    url_parts = url.split('?', 1)
    url = url_parts[0]
    query = f"?{url_parts[1]}" if len(url_parts) > 1 else ""

    if not url and query:
      return f"{relative_base_url}{query}"

    # get the base parts of the URL
    if relative_base_url:
      if not relative_base_url.startswith(('http://', 'https://')):
        if base_url:
          domain = base_url.split('/')[0:3]
          relative_base_url = '/'.join(domain) + '/' + relative_base_url
      base_parts = relative_base_url.split('/')
    else:
        base_parts = base_url.split('/') if base_url else []

    # root paths
    if url.startswith('/'):
      if base_url:
        domain = '/'.join(base_url.split('/')[0:3])
        return f"{domain}{url}{query}"
      return f"{url}{query}"

    # relative paths
    path_parts = []

    if base_parts:
      path_parts.extend(base_parts[0:3])

      if base_url:
        base_path = base_url.split('/')[3:]
        path_parts.extend(base_path)
      elif len(base_parts) > 3:
        path_parts.extend(base_parts[3:-1])

    # add the new path parts
    for part in url.split('/'):
      if not part or part == '.':
        continue
      elif part == '..':
        if len(path_parts) > 3:
          path_parts.pop()
      else:
        path_parts.append(part)

    result = '/'.join(path_parts)

    # normalize the result
    if not result.startswith(('http://', 'https://')):
        if base_url:
            domain = '/'.join(base_url.split('/')[0:3])
            result = f"{domain}/{result}"
        else:
            result = f"http://{result}"

    # removing double slashes
    result = result.replace('http://', 'HTTP_TEMP').replace('https://', 'HTTPS_TEMP')
    while '//' in result:
        result = result.replace('//', '/')
    result = result.replace('HTTP_TEMP', 'http://').replace('HTTPS_TEMP', 'https://')

    result = f"{result}{query}"

    return result.rstrip('/')

  @classmethod
  def parse(cls, url: str, relative_base_url=None, base_url=None):
    """
    parses a link into a URL object seperating href, inputs, and real url
    """
    raw_url = url

    # normalize url if needed
    if not url.startswith('http'):
      url = cls.fix_url(url, relative_base_url, base_url)
      if not url:
        warn(f"Failed to parse relative URL {raw_url}", bypass=True)
        return None

    full_url = url

    # parse query inputs
    inputs = {}
    parsed_url = url

    if "?" in parsed_url:
      parsed_url, query = parsed_url.split("?", 1)
      for pair in query.split("&"):
        if "=" in pair:
          key, value = pair.split("=", 1)
          inputs[key] = value

    # parse path inputs (e.g. /path/to/a=1/b=2)
    parsed_url_segments = parsed_url.split("/")
    for idx, segment in enumerate(parsed_url_segments):
      if "=" in segment:
        key = segment.split("=")[0]
        value = segment.split("=")[1]
        inputs[key] = value
        parsed_url = "/".join(parsed_url_segments[:idx])
        break

    return cls(raw_url=raw_url.split("?")[0], base_url=full_url, parsed_url=parsed_url, inputs=inputs)

  def __eq__(self, other):
    if not isinstance(other, self.__class__) and isinstance(other, str):
      return self.parsed_url == other
    return self.parsed_url == other.parsed_url

  def __hash__(self):
      return hash(self.parsed_url)

  def __str__(self):
    if self.has_inputs():
      return f"{self.parsed_url}?{'&'.join([f'{key}={value}' for key, value in self.inputs.items()])}"
    return self.parsed_url

  def __repr__(self):
    if self.has_inputs():
          return f"{self.parsed_url}??{'&'.join([f'{key}={value}' for key, value in self.inputs.items()])}"
    return self.parsed_url

class Input:
  """
  Represents an input field on a page with a name, type, and value
  """
  name: str
  type: str
  url: str

  def __init__(self, name: str, type: str, url: str, value = None):
    self.name = name
    self.type = type
    self.url = url
    self.value = value

  def is_button(self):
    return self.type == "submit"

  def __str__(self):
    return f"<{self.url}:{self.type}:{self.name}={self.value}>"

  def __repr__(self):
    return f"<{self.url}:{self.type}:{self.name}={self.value}>"

def validate_links(browser: StatefulBrowser, links: list[URL], base_url: str, ignore_pages: set = set(), blacklist: set = set()):
    """
    Validates a list of links
    1. Checks if the link is on the same site
    2. Checks if the link is recursive
    3. Checks if the link is real
    4. Follows the link to check if it is valid and get the output link e.g lh/admin.ph/something.ph -> lh/something.ph
    """
    parsed_base = urlparse(base_url)
    valid_links = set()

    for link in links:
      # check link is supposed to be ignored or blacklisted
      if link.parsed_url in ignore_pages or link.url in ignore_pages:
        debug(f"Link {link} is ignored")
        ignore_pages.add(link.parsed_url)
        continue

      if link.parsed_url in blacklist or link.url in blacklist or link.raw_url in blacklist:
        debug(f"Blacklisted link {link} at {base_url}")
        ignore_pages.add(link.parsed_url)
        continue

      # check link is on the same site
      if link.parsed_url.startswith(('http://', 'https://')):
        parsed_link = urlparse(link.parsed_url)
        if parsed_link.netloc != parsed_base.netloc:
          debug(f"Skipping external link {link}")
          continue

      # check link is not recursive
      link_groups = link.parsed_url.replace("//","/").split("/")
      if base_url.endswith(URL.fix_url(link.raw_url)) or len(link_groups) != len(set(link_groups)):
          warn(f"Link {link} is recursive {base_url} -> {link.raw_url}")
          ignore_pages.add(link.parsed_url)
          continue

      debug(f"Checking link {link} exists at {base_url}")

      # check link is real
      if not browser.open(link.parsed_url.replace("index.php/","")).status_code == 200:
        debug(f"Link {link} is not real")
        ignore_pages.add(link.parsed_url)
        continue

      # try to follow link
      try:
        browser.open(base_url)
        browser.follow_link(link.raw_url)

        # reinsert query inputs if exists
        current_url = (
            f"{browser.url}?{'&'.join([f'{key}={value}' for key, value in link.inputs.items()])}"
            if link.has_inputs() else browser.url)

        # check new url is still valid
        if URL.parse(current_url).parsed_url not in ignore_pages:
          valid_links.add(current_url)
          debug(f"Link {link} is valid")
        else:
          debug(f"Link {link} is ignored")
          continue
      except:
          warn(f"Failed to follow previously validated link {link} at {base_url}/{link.raw_url}")
          valid_links.add(link.url)

    return valid_links

def find_links_on_page(browser: StatefulBrowser, url: str, base_url: str, seen_pages: set = set(), blacklist: set = set()):
  """
  Gets all valid links on a page
  returns valid_links and all_parsed_links for future inputs searching usage
  """
  parsed_links = []
  try:
    browser.open(url)
    found_links = [link.get('href') for link in browser.links()]
    parsed_links =  [URL.parse(link, url, base_url) for link in found_links if link is not None]
  except Exception as e:
    warn(f"Failed to get links at {url} : {e}")

  valid_links = validate_links(browser, parsed_links, base_url, seen_pages, blacklist)
  return valid_links, parsed_links

def crawl_link(browser: StatefulBrowser, urls: set[str], base_url: str, blacklist: set[str] = set()):
  """
  Page Discovery - Page Crawling
  recursively finds all pages on a site
  """

  def crawl_link_rec(browser: StatefulBrowser, url: str, base_url: str, seen_pages: set[str] = set(), found_pages: set[str] = set(), all_parsed_links: list[URL] = None, blacklist=set()):
    if all_parsed_links is None:
      all_parsed_links = []

    # check if we have already seen this page
    if url in seen_pages:
      debug(f"Ignoring {url}")
      return found_pages, all_parsed_links
    seen_pages.add(url)

    # find all links on the page
    valid_pages, parsed_links = find_links_on_page(browser, url, base_url, seen_pages=seen_pages, blacklist=blacklist)
    all_parsed_links.extend(parsed_links)

    # check if we found any pages and add them to the list
    if not valid_pages:
      debug(f"No pages found on {url}")
      return found_pages, all_parsed_links
    found_pages.update(valid_pages)

    # recursively crawl each page
    for page in valid_pages:
      new_pages, new_links = crawl_link_rec(
        browser, page, base_url,
        seen_pages=seen_pages,
        found_pages=found_pages,
        all_parsed_links=all_parsed_links,
        blacklist=blacklist
      )
      found_pages.update(new_pages)

    return found_pages, all_parsed_links

  seen_pages = set()
  all_found_pages = set()
  all_parsed_links = []

  for url in progress_bar(urls, desc=f"{Colors.GREEN}Crawling Links{Colors.RESET}"):
    pages, links = crawl_link_rec(
      browser, url, base_url,
      seen_pages=seen_pages,
      found_pages=set(),
      all_parsed_links=all_parsed_links,
      blacklist=blacklist
    )
    all_found_pages.update(pages)

  return all_found_pages, all_parsed_links

def discover_pages(browser, url, common_words, extensions, blacklist):
  # Page Discovery - Page Guessing
  page_guesses: list[str] = [ f"{url}/{word + ext}" for (word, ext) in itertools.product(common_words, extensions) ]
  guessed_pages: set[str] = set()
  for page in progress_bar(page_guesses, desc=f"{Colors.GREEN}Guessing Pages{Colors.RESET}"):
    try:
      response = browser.open(page)
      if response.status_code == 200:
        guessed_pages.add(page)
    except: # might fail to open some pages, but we can ignore
      pass

  sorted(guessed_pages) # trying to make this more deterministic but not sure if python has ordered sets like this
  info(f"Guessed {len(guessed_pages)} pages")

  # Page Discovery - Link Crawling
  found_pages, all_parsed_links = crawl_link(browser, {url, *guessed_pages} - blacklist, base_url=url, blacklist=blacklist)
  info(f"Found {len(found_pages)} pages crawling")

  # combine and normalize guessed pages to found pages to avoid duplicates
  discovered_pages: list[URL] = [URL.parse(page, base_url=url) for page in (found_pages | guessed_pages)]

  return discovered_pages, guessed_pages, found_pages, all_parsed_links

def discover_inputs(browser: StatefulBrowser, url: str, discovered_links: list[URL], discovered_pages: list[URL]):
  # Input Discovery - URL Inputs
  inputs_in_urls: set[Input] = {
    Input(name, "text/url", link.parsed_url, val) for link in discovered_links
    if link and link.has_inputs() for name, val in link.inputs.items()
  }
  info(f"Parsed {len(inputs_in_urls)} inputs from urls")

  # Input Discovery - Form Inputs
  inputs_from_pages = set()
  for url in progress_bar(set(discovered_pages), desc=f"{Colors.GREEN}[INFO]: Discovering Inputs{Colors.RESET}"):
    try:
      browser.open(url.parsed_url)
      inputs_from_pages.update([Input(input.get('name'), input.get('type'), url.parsed_url, input.get('value')) for input in browser.page.find_all('input')])
      info(f"Found {len(inputs_from_pages)} inputs from pages")
    except Exception as e:
      warn(f"Failed to find inputs for page {url}: {e}", bypass=False)

  # Input Discovery - Cookies
  cookies_from_pages = set()
  try:
    browser.open(url)
    cookies_from_pages = { Input(name, "cookie", url, val) for name, val in browser.session.cookies.items() }
    info(f"Found {len(cookies_from_pages)} cookies from pages")
  except Exception as e:
    warn(f"Failed to find cookies for page {url}: {e}")

  # add all inputs together
  discovered_inputs: set[Input] = inputs_in_urls | inputs_from_pages | cookies_from_pages
  info(f"Discovered {len(discovered_inputs)} inputs {discovered_inputs}")

  return discovered_inputs, inputs_in_urls, inputs_from_pages, cookies_from_pages

def discover(url: str, custom_auth, common_words, extensions, is_part0=False):
  info(f"Discovering {url} with custom_auth={custom_auth}, {len(common_words)} common_words, {len(extensions)} extensions, is_part0={is_part0}")
  browser = mechanicalsoup.StatefulBrowser(user_agent='MechanicalSoup')
  blacklist = set()

  # authenticate
  if custom_auth:
    authenticate(browser, custom_auth, url, blacklist)

  # Part 0
  if is_part0:
    info(f"Part 0: Printing DVWA Homepage: {url}", bypass=True)
    browser.open(url)
    print(browser.get_current_page())

  info(f"Discovering Pages on {url}", bypass=True)
  discovered_pages, guessed_pages, found_pages, all_parsed_links = discover_pages(browser, url, common_words, extensions, blacklist)

  info(f"Discovering Inputs on {len(discovered_pages)} pages", bypass=True)
  discovered_inputs, url_inputs, page_inputs, cookie_inputs = discover_inputs(browser, url, all_parsed_links, discovered_pages)

  # Print Report
  print(display_discovery(
    url=url,
    guessed_pages=guessed_pages,
    found_pages=found_pages,
    discovered_pages=discovered_pages,
    inputs_in_urls=url_inputs,
    inputs_from_pages=page_inputs,
    cookies_from_pages=cookie_inputs,
    discovered_inputs=discovered_inputs
  ))

  return browser, discovered_pages, discovered_inputs

def display_discovery(url: str, guessed_pages: set[str], found_pages: set[str], discovered_pages: list[URL],
                          inputs_in_urls: set[Input], inputs_from_pages: set[Input], cookies_from_pages: set[Input], discovered_inputs: set[Input]) -> str:
  """
  Util to generate a colored and formatted fuzzer discovery report
  """

  # group inputs by their normalized URLs
  inputs_by_url = defaultdict(list)
  for input_obj in discovered_inputs - cookies_from_pages:
    normalized_url = normalize_url(input_obj.url)
    inputs_by_url[normalized_url].append(input_obj)

  # deduplicate inputs within each page
  deduplicated_inputs_by_url = defaultdict(list)
  for url, inputs in inputs_by_url.items():
    seen_inputs = set()
    for input_obj in inputs:
      key = (input_obj.type, input_obj.name, input_obj.value)
      if key not in seen_inputs:
        seen_inputs.add(key)
        deduplicated_inputs_by_url[url].append(input_obj)

  # Normalize discovered pages URLs and remove duplicates
  normalized_pages = {normalize_url(page.url): page for page in discovered_pages}
  normalized_guessed = {normalize_url(page) for page in guessed_pages}
  normalized_found = {normalize_url(page) for page in found_pages}

  # build report
  report = [
    create_section_header("FUZZER DISCOVERY REPORT"),
    f"Target URL: {Colors.BLUE}{url} {Colors.RESET}",

    create_section_header("DISCOVERY METRICS"),
    f"{Colors.BOLD}Total Unique Pages: {Colors.GREEN}{len(normalized_pages.keys())}{Colors.RESET}",
    f"└── Pages Found by Crawling: {Colors.GREEN}{len(normalized_found)}{Colors.RESET}",
    f"└── Pages Found by Guessing: {Colors.GREEN}{len(guessed_pages)}{Colors.RESET}",
    f"\n{Colors.BOLD}Guessed Pages:{Colors.RESET}",
    *[f"  • {Colors.BLUE}{page} {Colors.RESET}" for page in sorted(guessed_pages)],

    f"\n{Colors.BOLD}Total Unique Inputs: {Colors.GREEN}{len(discovered_inputs)}{Colors.RESET}",
    f"└── URL Parameters: {Colors.GREEN}{len(inputs_in_urls)}{Colors.RESET}",
    f"└── Form Inputs: {Colors.GREEN}{len(inputs_from_pages)}{Colors.RESET}",
    f"└── Cookies: {Colors.GREEN}{len(cookies_from_pages)}{Colors.RESET}\n",

    create_section_header("DISCOVERED PAGES AND INPUTS"),
  ]

  # group pages with inputs
  for page_url in sorted(normalized_pages.keys()):
    report.append(f"\n  • {Colors.BLUE}{page_url} {Colors.RESET}")
    page_inputs = deduplicated_inputs_by_url[page_url]
    if page_inputs:
      report.append("    Inputs:")
      report.extend([f"      - {format_input(input_obj)}" for input_obj in sorted(page_inputs, key=lambda x: (x.type, x.name))])

  # add cookies!
  if cookies_from_pages:
    report.extend([
      create_section_header("COOKIES"),
      *[f"  • {cookie.name}: {Colors.YELLOW}{cookie.value}{Colors.RESET}"
      for cookie in sorted(cookies_from_pages, key=lambda x: x.name)]
    ])

  report.append("\n" + Colors.PURPLE + "=" * 80 + Colors.RESET)
  return "\n".join(report)
