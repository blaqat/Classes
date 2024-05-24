"""
CSAPX Project1: Words
Author: Sean Strout @ RIT CS

Demonstrates usage of the argparse module for word_freq.py.

Usage:
$ python3 word_freq.py [--output #] [--plot] word file
"""

import argparse

def parse_command_line() -> argparse.Namespace:
    """
    Parse the command line arguments using argparse
    :return: the parsed command line arguments
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('word',
                        help='a word to display the overall ranking of')
    parser.add_argument('filename',
                        help='a comma separated value unigram file')
    parser.add_argument("-o", "--output",
                        help="display the top OUTPUT (#) ranked words by number of occurrences")
    parser.add_argument("-p", "--plot",
                        help="plot the word rankings from top to bottom based on occurrences",
                        action="store_true")
    return parser.parse_args()

def main():
    """The main method"""
    args = parse_command_line()
    print('filename:', args.word)
    print('filename:', args.filename)
    print('output:', int(args.output))
    print('plot:', args.plot)


if __name__ == '__main__':
    main()