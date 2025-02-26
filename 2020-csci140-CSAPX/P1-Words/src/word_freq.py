"""
CSAPX Project 1: Words
"The goal for this activity is to write a program, word freq.py, that can compute the popularity of a word in terms of the number of occurrences of all words over all years. The
relationship of the words, by rank, can be plotted as a log-log chart to reveal Zipf’s law."
$ python3 word_freq.py [-h] [-o OUTPUT] [-p] word filename
Author: Aiden Green
"""
from argparse import ArgumentParser as argParser
import sys
from matplotlib import pyplot as plot
from word_count import readFiles


def sortWordsByOcc(words: dict) -> dict:
    """
    sorts the dictionary by value
    :param words:
    :return: sorted words dict
    """
    return dict(sorted(words.items(), key=lambda x: x[1]))


def main():
    """
    The main function.
    :return: None
    """
    parser = argParser()
    parser.add_argument("-o", "--output", type=int, help="display the top OUTPUT (#) ranked words by number of occurrences")
    parser.add_argument("-p", "--plot", action='store_true', help="plot the word rankings from top to bottom based on occurrences")
    parser.add_argument("word", help="a word to display the overall ranking of")
    parser.add_argument("filename", help="a comma separated value unigram file")

    args = parser.parse_args()
    words = readFiles(args.filename)
    words = sortWordsByOcc(words)
    words_list = list(words.items())
    words_list.reverse()

    if words:
        if args.word not in words:
            sys.stderr.write("Error: " + args.word + " does not appear in " + args.filename + "\n")
        else:
            found_word = [word for word in words_list if word[0] == args.word][0]
            fw_rank = words_list.index(found_word) + 1
            if args.output:
                print(args.word+" is ranked #"+str(fw_rank))

                for i in range(0, args.output):
                    v = words_list[i]
                    print("#" + str(i + 1) + ":", v[0], "->", v[1])

            if args.plot:
                frequencies = list(words.values())
                frequencies.reverse()
                ranks = list(range(1, len(words)+1))

                plot.loglog(ranks, frequencies, marker=".")
                point = (fw_rank, found_word[1])
                plot.plot(point[0], point[1], 'r*')
                plot.annotate(found_word[0], xy=point, xytext=(point[0], point[1]+2))
                plot.title("Word Frequencies: " + args.filename)
                plot.xlabel("Rank of word(" + args.word + " is rank " + str(fw_rank) + ")")
                plot.ylabel("Total number of occurrences")
                plot.show()


if __name__ == '__main__':
    main()