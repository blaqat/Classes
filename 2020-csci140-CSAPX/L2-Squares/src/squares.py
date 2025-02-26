"""
CSAPX In Lab 2: Recursive Squares

A program that prompts for the depth of recursion and use that to draw squares.

author: Aiden Green
"""

import turtle


def draw_squares_rec(side_size: int, depth: int) -> None:
    """
    draw_squres_rec is a recursive function that draws a squares inside of squares based on depth
    :param side_size: int
    :param depth: int
    :return: None
    """
    old_color = turtle.color()[0]
    turtle.color((depth - 1) % 2 == 0 and "orange" or "blue")

    if depth > 0:
        for i in range(2):
            draw_squares_rec(int(side_size / 3), depth - 1)
            turtle.forward(side_size)
            turtle.left(90)
            turtle.forward(side_size)
            turtle.left(90)

    turtle.color(old_color)


def main() -> None:
    """
    prompts the user for depth and uses it to draw squares
    :return: None
    """
    draw_squares_rec(300, int(input("Depth: ")))


if __name__ == '__main__':
    main()

