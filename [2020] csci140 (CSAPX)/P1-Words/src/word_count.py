"""
CSAPX Project 1: Words
"The goal for this activity is to write a program, word count.py, that is be able to count the
total number of occurrences of a word across all years."
$ python3 word_count.py [-h] word filename
Author: Aiden Green
"""
from argparse import ArgumentParser as argParser
import sys


def readFiles(fileName: str) -> dict:
    """
    Converts a file into a dictionary of {word: occurrences of word}
    :param fileName: str
    :return: dict {str, int}
    """
    words = {}

    try:
        with open(fileName) as file:
            for line in file:
                word, year, occ = line.split(", ")
                if word not in words:
                    words[word] = int(occ)
                else:
                    words[word] += int(occ)

    except:
        sys.stderr.write("Error: "+fileName+" does not exist!\n")

    return words


def main():
    """
    The main function.
    :return: None
    """
    parser = argParser()
    parser.add_argument("word", help="a word to display the total occurrences of")
    parser.add_argument("filename", help="a comma separated value unigram file")

    args = parser.parse_args()
    words = readFiles(args.filename)

    if words:
        if args.word not in words:
            sys.stderr.write("Error: "+args.word+" does not appear!\n")
        else:
            print(args.word+": ", words[args.word])


if __name__ == '__main__':
    main()