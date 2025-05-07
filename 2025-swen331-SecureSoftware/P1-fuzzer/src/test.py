from mechanicalsoup.stateful_browser import StatefulBrowser
from src.utils import info, debug, warn, create_section_header, Colors, format_input, normalize_url, Timer, progress_bar
from src.discover import discover, Input
from http.client import responses
from enum import Enum

# Globals ----------------------------------------
MS_TIMER = Timer(1000)
LOAD_TIME_THRESHOLD = 500
SENSITIVE_WORDS = []
UNSANITIZED_STRS = []

# Classes ----------------------------------------
class VulnerabilityType(Enum):
  """
  Represents the types of vulnerabilities found.
  """
  SANITIZATION = "Lack of Sanitization"
  SENSITIVE_DATA_LEAK = "Sensitive Data Leak"
  DELAYED_RESPONSE = "Delayed Response"
  HTTP_ERROR = "HTTP Error"

class TestInputs:
  """
  Represents a set of inputs for a single URL.
  """
  url: str
  form_inputs: list[Input]
  form_submits: list[Input]
  url_inputs: list[Input]
  buttons: list[Input]
  cookies: list[Input]

  def __init__(self, url: str):
    self.url = url
    self.form_inputs = []
    self.form_submits = []
    self.url_inputs = []
    self.buttons = []
    self.cookies = []

  @classmethod
  def from_inputs(cls, inputs: set[Input]) -> dict:
    """
    Converts a list of inputs into a dictionary of grouped inputs.
    """
    test_inputs = {}

    for input in inputs:
      url = normalize_url(input.url)
      if not url in test_inputs:
        debug(f"Creating new test data for {url}")
        test_inputs[url] = cls(url)
      if input.type in ("text", "password", "file"):
        test_inputs[url].form_inputs.append(input)
      elif input.type == "submit":
        test_inputs[url].form_submits.append(input)
      elif input.type == "text/url":
        debug(input, url)
        test_inputs[url].url_inputs.append(input)
      elif input.type == "button":
        test_inputs[url].buttons.append(input)
      elif input.type == "cookie":
        test_inputs[url].cookies.append(input)

    return test_inputs

  def __repr__(self):
    return f"TestData({self.url}, {self.form_inputs}, {self.form_submits}, {self.url_inputs}, {self.buttons}, {self.cookies})"

class Vulnerability:
  """
  Represents a vulnerability found on a page.
  """
  type: VulnerabilityType
  response: str
  response_code: int
  value: str

  def __init__(self, vulnerability: VulnerabilityType, response: int, value: str, input = None):
    self.type = vulnerability
    self.response = prettify_response(response)
    self.response_code = response
    self.value = value
    self.input = input

  def __str__(self):
    return f"{self.type}: \n\t{self.response} \n\tVector: ({self.value})"

  def __repr__(self):
    return f"TestData({self.type}, {self.response}, {self.value}, {self.input})"

  def __eq__(self, other):
    return self.type == other.type and self.response == other.response and self.input == other.input

  def __hash__(self):
    return hash((self.type, self.response, self.input))

class PageData:
  """
  Represents a page's data, including its URL, inputs, and vulnerabilities.
  """
  url: str
  inputs = list[TestInputs]
  vulns = list[Vulnerability]

  def __init__(self, url: str, inputs: list[TestInputs], vulns: list[Vulnerability]):
    self.url = url
    self.inputs = inputs
    self.vulns = vulns

  def __str__(self):
    """
    Prints the Test Data Report for a page
    """
    report = [
        f"{Colors.BLUE}{self.url}{Colors.RESET}"
    ]

    if self.vulns:
      # Print each vulnerability grouped by types
        vulns_by_type = {}
        for vuln in set(self.vulns):
            if vuln.type not in vulns_by_type:
                vulns_by_type[vuln.type] = []
            vulns_by_type[vuln.type].append(vuln)

        passed_first = False
        for vuln_type, vulns in vulns_by_type.items():
          if passed_first:
            report.append("")
          else:
            passed_first = True
          report.append(f"{Colors.BOLD}{vuln_type.value}:{Colors.RESET}")
          report_set = set()
          for vuln in vulns:
            vul_report = ("  • " +
              (f"Input: {format_input(vuln.input, include_value=False)} | " if vuln.input else "") +
              (f"Value: {Colors.YELLOW}{vuln.value}{Colors.RESET}" if vuln.value else "") +
              (f" | HTTP Response: {vuln.response}" if vuln.response and vuln.response_code != 200 else ""))
            report_set.add(vul_report)
          report.append("\n".join(report_set))
    else:
        report.append(f"{Colors.ITALIC}{Colors.GREEN}No vulnerabilities found!{Colors.RESET}")

    report.append("\n" + Colors.PURPLE + "=" * 80 + Colors.RESET)

    return "\n".join(report)

# Test Command Utilities ----------------------------------------
def prettify_response(response_code: int) -> str:
  """
  Colors the response code
  """
  response_description = responses[response_code]
  response = f"[{response_code}] {response_description}"
  if response_code in range(200, 300):
    return Colors.GREEN + response + Colors.RESET
  elif response_code in range(300, 400):
    return Colors.YELLOW + response + Colors.RESET
  else:
    return Colors.RED + response + Colors.RESET

def check_sanitization(vulns: list[Vulnerability], unsanitized_text: str, browser: StatefulBrowser, response_func, *params, input = None):
  resp = response_func(*params)
  url = browser.url
  if unsanitized_text in resp.text:
    vulns.append(
      Vulnerability(VulnerabilityType.SANITIZATION, resp.status_code, unsanitized_text, input))
    warn(f"Sanitization bypass detected for {url}")

def check_response(vulns: list[Vulnerability], response_func, *params, input = None):
  MS_TIMER.start()
  response = response_func(*params)
  load_time = MS_TIMER.stop()

  # checks page loading time
  if load_time > LOAD_TIME_THRESHOLD:
    vulns.append(
      Vulnerability(VulnerabilityType.DELAYED_RESPONSE, response.status_code, str(load_time), input))
    warn(f"Page took {load_time}ms to load")

  # checks response is error
  if response.status_code != 200:
      vulns.append(
        Vulnerability(VulnerabilityType.HTTP_ERROR, response.status_code, response.url, input))
      warn(f"Unexpected response code {response.status_code} for {response.url}")

  # checks sensitive words in response
  for sensitive_word in SENSITIVE_WORDS:
    if sensitive_word in response.text:
      vulns.append(
        Vulnerability(VulnerabilityType.SENSITIVE_DATA_LEAK, response.status_code, sensitive_word, input))
      warn(f"Sensitive data {sensitive_word} found in response")

# Test Command Functions ----------------------------------------
def test_page_inputs(browser: StatefulBrowser, inputs: TestInputs, vulns: list[Vulnerability]):
  # tests page inputs
  for input in inputs.form_inputs:
    try:
      for vector in VECTORS:
        browser.select_form('form')
        browser[input.name] = vector
        check_response(vulns, browser.submit_selected, input=input)
      for char in UNSANITIZED_STRS:
        browser.select_form('form')
        browser[input.name] = char
        check_sanitization(vulns, char, browser, browser.submit_selected, input=input)
    except Exception as e:
      warn(f"Failed to test form input {input.name}: {e}")

  # tests url queries
  for input in inputs.url_inputs:
    try:
      url = f"{input.url}?{input.name}="
      for vector in VECTORS:
        check_response(vulns, browser.open, url + vector, input=input)
      for char in UNSANITIZED_STRS:
        check_sanitization(vulns, char, browser, browser.open, url + char, input=input)
    except Exception as e:
      warn(f"Failed to test URL parameter {input.name}: {e}")

  # tests cookies
  for cookie in inputs.cookies:
    try:
      for vector in VECTORS:
        browser.session.cookies.set(cookie.name, vector)
        check_response(vulns, browser.refresh, input=cookie)
      for char in UNSANITIZED_STRS:
        browser.session.cookies.set(cookie.name, char)
        check_sanitization(vulns, char, browser, browser.refresh, input=cookie)
    except Exception as e:
      warn(f"Failed to test cookie {cookie.name}: {e}")

  return vulns

def test(url, custom_auth, common_words, extensions, vectors, sanitized_chars, sensitive, slow):
  global SENSITIVE_WORDS, LOAD, LOAD_TIME_THRESHOLD, UNSANITIZED_STRS, VECTORS
  SENSITIVE_WORDS = sensitive
  LOAD_TIME_THRESHOLD = slow
  UNSANITIZED_STRS = list(map(lambda c: f"FUZZ{c}FIZZ" if len(c) == 1 else c, sanitized_chars))
  VECTORS = vectors

  # Calling discover to get pages and inputs
  browser, discovered_pages, discovered_inputs = discover(url, custom_auth, common_words, extensions)
  inputs = TestInputs.from_inputs(discovered_inputs)
  pages = {normalize_url(page.parsed_url): PageData(url=normalize_url(page.parsed_url), inputs=inputs.get(normalize_url(page.parsed_url), TestInputs(normalize_url(page.parsed_url))), vulns=list()) for page in discovered_pages}

  # Find vulnerabilies on pages
  for page in progress_bar(pages.values(), desc=f"{Colors.GREEN}[INFO]: Testing {url} for vulnerabilities{Colors.RESET}"):
    info(f"Testing page {page.url}")
    # Check page for Response Loading, Response Error, and Sensitive Data Leaks
    check_response(page.vulns, browser.open, page.url)
    # Test Inputs for Vectors and Unsanitization
    test_page_inputs(browser, page.inputs, page.vulns)

  # Print out the vulnerability test report
  print(create_section_header("FUZZER TEST REPORT"))
  for k, page in pages.items():
    print(page)
