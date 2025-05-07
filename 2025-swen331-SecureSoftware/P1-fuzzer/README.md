# Fuzzer
This is a simple fuzzer project to learn about security vulnerabilities and how to exploit them.

# Prereqs
- Python 3.9

- Python Libraries
  - mechanicalsoup
  - argparse
  - pathlib
  - tqdm

# Part-0 Usage
```sh
python3 fuzz.py discover <url> --custom-auth=dbwa --part0 --common-words=cfg/common_words.txt --verbose
```
- **Must** use the `--part0` flag to get the proper dvwa homepage output
- **Must** include `--common-words` flag because discover requires it, but it **will not** be used in part0 so it can be left blank
- Optionally include `--verbose` flag for more detailed output

# Part-1 Discover Usage
*same as part 0 but with out the `--part0` flag*
```sh
python3 fuzz.py discover <url> --custom-auth=dbwa --common-words=cfg/common_words.txt --extensions=cfg/extensions.txt --verbose
```
- Optionally include `--extensions` flag
- Optionally include `--verbose` flag for more detailed output
- **Must** include `--common-words` flag because discover requires it
=======
- Optionally include `--verbose` flag for more detailed output (Not used in CI)

# Part-2 Test Usage
```sh
python3 fuzz.py test <url> --custom-auth=dbwa --common-words=cfg/common_words.txt --extensions=cfg/extensions.txt --vectors=cfg/vectors.txt --sanitized-chars=cfg/sanitized_chars.txt --sensitive=cfg/sensitive.txt --slow=500
```
- **Required** `--common-words=file` Newline-delimited file of common words to be used in page guessing.
- **Required** `--sensitive=file` Newline-delimited file data that should never be leaked. It's assumed that this data is in the application's database (e.g. test data), but is not reported in any response.
- **Required** `--vectors=file` Newline-delimited file of common exploits to vulnerabilities.
- **Optional** `--extensions=file` Newline-delimited file of path extensions, e.g. ".php". Defaults to ".php" and the empty string if not specified
- **Optional**`--sanitized-chars=file` Newline-delimited file of characters that should be sanitized from inputs. Defaults to just < and >
- **Optional** `--slow=500` Number of milliseconds considered when a response is considered "slow". Default is 500 milliseconds
