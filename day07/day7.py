import sys
from antlr4 import *
from collections import defaultdict
from day7Lexer import day7Lexer
from day7Parser import day7Parser

# To run:
# antlr4 -Dlanguage=Python3 day7.g4
# python3 day7.py input

class ContainedBag:
    bag: str
    quantity: int

    def __init__(self, bag, quantity):
        self.bag = bag
        self.quantity = quantity

    def __repr__(self):
        return "Bag: " + self.bag + ", quantity: " + str(self.quantity)

def has_method(o, name):
    return callable(getattr(o, name, None))


def getSyntaxTree(fileName):
    input_stream = FileStream(fileName)
    lexer = day7Lexer(input_stream)
    stream = CommonTokenStream(lexer)
    parser = day7Parser(stream)
    return parser.sentences()


def countParents(containers, unique_containers, current):
    if current in unique_containers:
        return unique_containers
    unique_containers.add(current)
    if current in containers:
        for outerBag in containers[current]:
            countParents(containers, unique_containers, outerBag)

    return unique_containers


def countChildren(containers, current, count):
    levelCount = 0
    if current in containers:
        for child in containers[current]:
            childCount = count * child.quantity
            levelCount = levelCount + childCount + countChildren(containers, child.bag, childCount)

    return levelCount


def main(argv):
    tree = getSyntaxTree(argv[1])

    containers = defaultdict(list)
    children = defaultdict(list)

    for sentence in tree.sentence():
        if not has_method(sentence.rules().containsRules(), "containsRule"):
            continue

        for rule in sentence.rules().containsRules().containsRule():
            containers[rule.color().getText()].append(sentence.color().getText())
            children[sentence.color().getText()].append(
                ContainedBag(rule.color().getText(), int(rule.NUMBER().getText()))
            )

    # part 1
    unique_containers = countParents(containers, set(), "shiny gold")
    unique_containers.discard("shiny gold")
    print(len(unique_containers))

    # part 2
    print(countChildren(children, "shiny gold", 1))

if __name__ == '__main__':
    main(sys.argv)
