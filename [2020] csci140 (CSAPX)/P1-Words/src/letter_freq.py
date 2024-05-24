"""
CSAPX Project 1: Words
"The goal for this activity is to write a program, letter freq.py, that can compute the
frequency of each letter, a-z, across the total occurrences of all words over all years."
$ python3 letter_freq.py [-h] [-o] [-p] filename
Author: Aiden Green
"""
from argparse import ArgumentParser as argParser
import sys
from matplotlib import pyplot as plot


def readFiles(fileName: str) -> tuple:
    """
    Converts a file into a dictionary of {letter: occurrences of letter in file}
    and returns the dictionary and total letters from the file
    :param fileName: str
    :return: tuple (dict {char: int}, int)
    """
    letters = {}
    total_letters = 0

    try:
        with open(fileName) as file:
            for line in file:
                word, year, occ = line.split(", ")
                word = word.lower()
                occ = int(occ)
                for letter in word:
                    if letter not in letters:
                        letters[letter] = occ
                    else:
                        letters[letter] += occ
                    total_letters += occ
    except:
        sys.stderr.write("Error: "+fileName+" does not exist!\n")

    return letters, total_letters


def getLetterFrequencies(letters: dict, total: int) -> dict:
    """
    returns a new list of letters a-z with the frequencies of the letters from the passed in list
    :param letters:
    :param total:
    :return:
    """
    new_letters = list("abcdefgihjklmopqrstuvwxyz")
    freqs = []
    total = total or sum(letters.values())
    for letter in new_letters:
        freqs.append(letter in letters and (letters[letter] / total) or 0.0)

    return dict(zip(new_letters, freqs))

def main():
    """
    The main function.
    :return: None
    """
    parser = argParser()
    parser.add_argument("-o", "--output", action='store_true', help="display latter frequencies to standard output")
    parser.add_argument("-p", "--plot", action='store_true', help="plot letter frequencies using matplotlib")
    parser.add_argument("filename", help="a comma separated value unigram file")

    args = parser.parse_args()
    letters, total = readFiles(args.filename)
    frequencies = getLetterFrequencies(letters, total)

    if letters:
        if args.output:
            for letter, freq in frequencies.items():
                print(letter+":", freq)

        if args.plot:
            plot.bar(frequencies.keys(), frequencies.values())
            plot.title("Letter Frequencies: "+args.filename)
            plot.xlabel("Letter")
            plot.ylabel("Frequency")
            plot.show()


if __name__ == '__main__':
    main()