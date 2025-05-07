import argparse
from pathlib import Path
from src.discover import discover
from src.test import test
import src.utils as utils
from src.utils import read_file

DEFAULT_EXTENSIONS = [".php", ""]
DEFAULT_SANITIZED_CHARS = ["<", ">"]
DEFAULT_SLOW = 500


# Main-------------------------------------------------------
def main(args):
  command = args.command
  url = args.url
  utils.VERBOSE = args.verbose

  extensions = read_file(args.extensions) if args.extensions is not None else DEFAULT_EXTENSIONS
  common_words = read_file(args.common_words, args.part0)

  # Run Command
  if command == 'discover':
    discover(url=url, custom_auth=args.custom_auth, common_words=common_words, extensions=extensions, is_part0=args.part0)
  elif command == 'test':
    vectors = read_file(args.vectors)
    sanitized_chars = read_file(args.sanitized_chars) if args.sanitized_chars is not None else DEFAULT_SANITIZED_CHARS
    sensitive = read_file(args.sensitive)
    test(url=url, custom_auth=args.custom_auth, common_words=common_words, extensions=extensions, vectors=vectors, sanitized_chars=sanitized_chars, sensitive=sensitive, slow=args.slow)

  exit()

if __name__ == "__main__":
  arg_parser = argparse.ArgumentParser(prog='fuzzer')

  # Default Arguments
  default_arg_parser = argparse.ArgumentParser(add_help=False)
  default_arg_parser.add_argument('url', type=str)
  default_arg_parser.add_argument('--custom-auth', type=str)
  default_arg_parser.add_argument('--verbose', action='store_true')
  default_arg_parser.add_argument('--part0', action='store_true')

  # Commands
  command_opt_parser = arg_parser.add_subparsers(dest='command')

  # Discover Options
  discover_arg_parser = command_opt_parser.add_parser('discover', parents=[default_arg_parser])
  discover_arg_parser.add_argument('--common-words', type=Path, required=True)
  discover_arg_parser.add_argument('--extensions', type=Path, required=False)

  # Test Options
  test_arg_parser = command_opt_parser.add_parser('test', parents=[default_arg_parser])
  test_arg_parser.add_argument('--common-words', type=Path, required=True)
  test_arg_parser.add_argument('--extensions', type=Path, required=False)
  test_arg_parser.add_argument('--vectors', type=Path, required=True)
  test_arg_parser.add_argument('--sanitized-chars', type=Path, required=False)
  test_arg_parser.add_argument('--sensitive', type=Path, required=True)
  test_arg_parser.add_argument('--slow', type=int, required=False, default=DEFAULT_SLOW)

  main(arg_parser.parse_args())
