import sys
from antlr4 import *
from day8Lexer import day8Lexer
from day8Parser import day8Parser


class Instruction:
    command: str
    number: int

    def __init__(self, command, number):
        self.command = command
        self.number = number

    def __repr__(self):
        return "Comand: " + self.command + ", number: " + str(self.number)


def getSyntaxTree(fileName):
    input_stream = FileStream(fileName)
    lexer = day8Lexer(input_stream)
    stream = CommonTokenStream(lexer)
    parser = day8Parser(stream)
    return parser.commands()


def main(argv):
    tree = getSyntaxTree(argv[1])

    program = {}

    i = 0
    for command in tree.command():
        program[i] = Instruction(command.KEYWORD().getText(), int(command.NUMBER().getText()))
        i = i +1

    print(execute(program)[0])
    print(fixInstruction(program))


def execute(program):
    visited = set()

    acc = 0
    current = 0
    while not current in visited:
        visited.add(current)
        if not current in program:
            return acc, True

        instruction = program[current]

        if instruction.command == 'acc':
            acc = acc + instruction.number
            current = current + 1
        elif instruction.command == 'jmp':
            current = current + instruction.number
        else:
            current = current + 1

    return acc, False


def fixInstruction(program):
    for i in range(len(program)):
        copy = dict(program)
        instruction = program[i]

        if instruction.command == 'acc':
            continue

        if instruction.command == 'jmp':
            copy[i] = Instruction('nop', instruction.number)
        else:
            copy[i] = Instruction('jmp', instruction.number)

        acc, terminated = execute(copy)
        if terminated:
            return acc


if __name__ == '__main__':
    main(sys.argv)
