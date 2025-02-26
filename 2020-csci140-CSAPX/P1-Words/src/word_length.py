"""
CSAPX Project 1: Words
"The goal for this activity is to write a program, word length.py, that can compute the
average word length over a range of years. These results can be plotted as a line chart."
$ python3 word_length.py [-h] [-o] [-p] start end filename
Author: Aiden Green
"""
from argparse import ArgumentParser as argParser
import sys
from matplotlib import pyplot as plot


def readFiles(fileName: str, year_start: int, year_end: int) -> dict:
    """
    Converts a file.csv into a dictionary of {year (int): set {words (str) used in year} }
    if the year is inbetween year_start and year_end
    :param fileName: str
    :param year_start: int
    :param year_end: int
    :return: dict {int, set}
    """
    years = {}

    try:
        with open(fileName) as file:
            for line in file:
                word, year, occ = line.split(", ")
                occ = int(occ)
                if year_start <= year <= year_end:
                    if year not in years:
                        years[year] = {word: occ}
                    else:
                        years[year][word] = occ
    except:
        sys.stderr.write("Error: "+fileName+" does not exist!\n")

    return dict(sorted(years.items()))


def getAverageWordLength(words: dict) -> float:
    """
    returns the average word length from a dictionary of {words: occurrences}
    :param words: dict
    :return: float average word length
    """
    return sum(len(word) * words[word] for word in words) / (sum(words.values()))


def main():
    """
    The main function.
    :return: None
    """
    parser = argParser()
    parser.add_argument("-o", "--output", action='store_true', help="display the top OUTPUT (#) ranked words by number of occurrences")
    parser.add_argument("-p", "--plot", action='store_true', help="plot the word rankings from top to bottom based on occurrences")
    parser.add_argument("start", help="the starting year range")
    parser.add_argument("end", help="the ending year range")
    parser.add_argument("filename", help="a comma separated value unigram file")

    args = parser.parse_args()

    if args.start > args.end:
        sys.stderr.write("Error: start year must be less than or equal to end year!")
    else:
        years = readFiles(args.filename, args.start, args.end)
        if years:
            years = {year: getAverageWordLength(years[year]) for year in years}

            if args.output:
                for year, lth in years.items():
                    print(year+":", lth)

            if args.plot:
                plot.plot(years.keys(), years.values())
                plot.title("Average word lengths from " + args.start + " to " + args.end + ": " + args.filename)
                plot.xlabel("Year")
                plot.ylabel("Average word length")
                plot.show()


if __name__ == '__main__':
    main()