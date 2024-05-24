"""
CSAPX Lab 2: Recursive Squares

A program that prompts for the depth of recursion and use that to draw squares and more squares inside of those squares.

author: Aiden Green
"""

import snippet
import turtle
from math import sqrt

def isEven(number: int) -> bool:
    """
    isEven returns true if the number is Even and False if the number is odd
    :param number:
    :return: bool
    """
    return number % 2 == 0


def draw_squares_rec(side_size: float, depth: int) -> None:
    """
    draw_squres_rec is a recursive function that draws 90 degree rotated squares inside of squares based on depth
    :param side_size: float
    :param depth: int
    :return: None
    :post: turtle is where it started, facing the direction it started in, pen up
    """
    turtle.pendown()
    old_color = turtle.color()[0]
    turtle.color(isEven(depth - 1) and "orange" or "blue")

    if depth > 0:
        for i in range(4):
            even = isEven(i)
            turtle.left(even and 90 or 135)
            turtle.forward(side_size / 2)
            turtle.right(even and 135 or 90)
            if even:
                draw_squares_rec(side_size * snippet.SHRINKAGE, depth-1)
                turtle.pendown()
            else:
                turtle.forward(side_size)
                turtle.right(180)

    turtle.color(old_color)
    turtle.penup()


def main() -> None:
    """
    prompts the user for depth and uses it to draw squares
    :return: None
    """
    snippet.init(snippet.BOX_SIZE, 3)
    snippet.SHRINKAGE = 1 / (2 * sqrt(2))
    draw_squares_rec(snippet.BOX_SIZE, int(input("Depth: ")))
    snippet.finish()


if __name__ == '__main__':
    main()