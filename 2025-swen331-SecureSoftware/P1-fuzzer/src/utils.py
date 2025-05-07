from pathlib import Path
import time
from tqdm import tqdm

VERBOSE = False

# Helper Functions-------------------------------------------------------
class Timer:
  def __init__(self, multiplier=1):
    self.start_time = None
    self.multiplier = multiplier

  def start(self):
    self.start_time = time.time() * self.multiplier

  def stop(self):
    if self.start_time is None:
      error("Timer not started")
      return -1
    end_time = time.time() * self.multiplier
    return end_time - self.start_time

class Colors:
    PURPLE = '\033[95m'
    BLUE = '\033[94m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    CYAN = '\033[96m'
    BLUE = '\033[94m'
    RED = '\033[91m'
    RESET = '\033[0m'
    BOLD = '\033[1m'
    ITALIC = '\033[3m'
    UNDERLINE = '\033[4m'

def progress_bar(iterable, desc, bypass=True):
  if not VERBOSE or bypass:
    return tqdm(iterable, desc=f"{Colors.GREEN}[INFO]: {desc} {Colors.RESET}")

def error(*args, bypass=True, **kwargs):
  if VERBOSE or bypass:
    print(f"{Colors.RED}[ERROR]:", *args, **kwargs, end=f"{Colors.RESET}\n")

def info(*args, bypass=False, **kwargs):
  if VERBOSE or bypass:
    print(f"{Colors.GREEN}[INFO]:", *args, **kwargs, end=f"{Colors.RESET}\n")

def warn(*args, bypass=False, **kwargs):
  if VERBOSE or bypass:
    print(f"{Colors.YELLOW}[WARN]:", *args, **kwargs, end=f"{Colors.RESET}\n")

def debug(*args, bypass=False, **kwargs):
  if VERBOSE or bypass:
    print(f"{Colors.BLUE}[DEBUG]:", *args, **kwargs, end=f"{Colors.RESET}\n")

def create_section_header(title: str) -> str:
    separator = Colors.PURPLE + '=' * 80 + Colors.RESET
    return f"\n{separator}\n{Colors.PURPLE}{Colors.BOLD}{title}{Colors.RESET}\n{separator}"

def format_input(input_obj, include_value=True) -> str:
  return f"[{Colors.CYAN}{Colors.ITALIC}{input_obj.type}{Colors.RESET}] {input_obj.name}" + (f": {Colors.YELLOW}{input_obj.value}{Colors.RESET}" if include_value else "")

def normalize_url(url: str) -> str:
  if not isinstance(url, str):
    url = url.parsed_url
  # remove query parameters and trailing slashes
  base = url.split('?')[0]
  base = base.rstrip('/')
  return base

def read_file(path: Path, is_part0=False):
  try:
    with open(path, 'r') as f:
      return [line.strip() for line in f.readlines()]
  except FileNotFoundError:
    if is_part0:
      warn(f"File not found: {path}. But IGNORING as this is not used in part-0", bypass=True)
    else:
      error(f"File not found: {path}")
      exit()
  except IsADirectoryError:
    if is_part0:
      warn(f'Path "{path}" is a directory not a file:. But IGNORING as this is not used in part-0', bypass=True)
    else:
      error(f'Path "{path}" is a directory not a file')
      exit()
  except Exception as e:
    if is_part0:
      warn(f"Error reading file: {e}. But IGNORING as this is not used in part-0", bypass=True)
    else:
      error(f"Error reading file: {e}")
      exit()
