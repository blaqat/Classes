from mechanicalsoup.stateful_browser import StatefulBrowser
from src.utils import info
from src.utils import error

# Custom Auth Functions-------------------------------------------------------
def dvwa_auth(browser: StatefulBrowser, url, blacklist=set()):
  USERNAME = "admin"
  PASSWORD = "password"
  info(f"Authenticating DVWA at {url}")

  # 1. Go to {URL}/setup.php where {URL} is the given url from the command line that points to a DVWA instance
  browser.open(f"{url}/setup.php")

  # 2. “Click” on the Create/Reset Database (i.e. submit the form)
  info("Creating/Resetting Database")
  browser.select_form('form')
  browser.submit_selected()

  # 3. Go to {URL}, and it will forward you to the login page
  browser.open(url)

  # 4. Enter in “admin” and “password”
  browser.select_form('form')
  browser["username"] = USERNAME
  browser["password"] = PASSWORD

  # 5. “Click” Login (i.e. submit the form)
  info("Logging In")
  browser.submit_selected()

  # 6. Go to the DVWA Security page ({URL}/security.php)
  browser.open(f"{url}/security.php")

  # 7. Select “Low” and submit the form
  info("Setting Security to Low", bypass=True)
  browser.select_form('form')
  browser["security"] = "low"
  browser.submit_selected()

  # 8. Begin your fuzzing operations. (See rest of instructions)
  info("DVWA Authenticated", bypass=True)
  blacklist.add("http://localhost/logout.php")
  blacklist.add("http://localhost/index.php/logout.php")
  blacklist.add("logout.php")

CUSTOM_AUTHS = {
  "dvwa": dvwa_auth
}

def authenticate(browser: StatefulBrowser, auth, url, blacklist):
  """
  Runs the stored procedure for specific websites to authenticate the user.
  """
  info(f"Authenticating Custom Auth at {url}")
  try:
    CUSTOM_AUTHS[auth](browser, url, blacklist)
  except KeyError:
    if auth is not None:
      error(f"Custom auth '{auth}' not found")
      exit()
  except Exception as e:
    error(f"Custom auth '{auth}' at {url} failed: {e}")
    exit()
