"""
CSAPX Lab 1: Tiny Turtle

A program that takes basic TT commands and then does turtle drawing from it.

author: Aiden Green
"""
import turtle

ttCommands = {
    "F": "forward",
    "B": "backward",
    "L": "left",
    "R": "right",
    "C": "circle",
    "U": "up",
    "D": "down",
}


def runCommand(ttCommand: str, argument = None) -> None:
    """
    If argument exists:
        The runCommand gets the Turtle function associated with the ttCommand
        then calls the function with the argument given
        then prints out the command that was ran.

    If argument doesnt exist:
        The runCommand splits the ttCommand string into a ttCommand letter, and arguments.
        then calls runCommand(letter, arguments)
    :param ttCommand: str
    :param argument: int
    :return: None
    """
    if not argument:
        runCommand(ttCommand[0].capitalize(), int(ttCommand[1:] or -1))
    else:
        func = None
        try:
            ttCommand = ttCommands[ttCommand]
            func = getattr(turtle, ttCommand)
        except:
            print(ttCommand + "is not a valid command")
        else:
            try:
                func(argument)
                print(ttCommand + "(" + str(argument) + ")")
            except TypeError:
                func()
                print(ttCommand + "()")


def stringToProgram(string: str) -> list:
    """
    The stringToProgram converts a string of ttCommands
    :param string: str
    :return: list
    """
    program = []
    string = string.strip()

    while string.find(" ") > 0:
        command = string[:string.index(" ")]
        program.append(command)
        string = string[len(command)+1:].strip()
    program.append(string)

    return program


def runProgram(program: list) -> None:
    """
    The runProgram function prints the program.
    Then calls the runCommand(command) function for every command in the program list argument.
    :param program: list
    :return: None
    """
    print("Program:", " ".join(program))

    for command in program:
        runCommand(command)


def main() -> None:
    """
    The main function prompts the user to enter a TT program.  It then expands
    that program to the basic TT commands and then executes them.
    :return: None
    """

    commands = input("Enter Tiny Turtle program (CMD+D or CTRL+D to terminate):")
    program = stringToProgram(commands)

    runProgram(program)
    print('Close the graphic window when done.')
    turtle.mainloop()


def main_2(c_name=None) -> None:
    """
    The main function prompts the user to enter a TT program.  Except how I would do it
    :return: None
    """
    commands = input("Enter Tiny Turtle program (CMD+D or CTRL+D to terminate):").strip().split(" ")
    runProgram(commands)

    print('Close the graphic window when done.')
    turtle.mainloop()


if __name__ == '__main__':
    main()
