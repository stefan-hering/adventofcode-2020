package main

import (
	"bufio"
	"log"
	"os"
	"strconv"
	"strings"
)

type Rule struct {
	num1 int
	num2 int
	char string
}

func main() {
	f, _ := os.Open("./input")
	defer f.Close()

	scanner := bufio.NewScanner(f)

	counter1 := 0
	counter2 := 0

	for scanner.Scan() {
		line := strings.Split(scanner.Text(), ":")
		rule := strings.Split(line[0], " ")
		counts := strings.Split(rule[0], "-")
		num1, _ := strconv.Atoi(counts[0])
		num2, _ := strconv.Atoi(counts[1])

		r := Rule{num1, num2, rule[1]}

		part1(line[1], r, &counter1)
		part2(line[1], r, &counter2)
	}

	log.Print(counter1)
	log.Print(counter2)
}

func part1(password string, rule Rule, counter *int) {
	charCount := strings.Count(password, rule.char)

	if charCount >= rule.num1 && charCount <= rule.num2 {
		*counter = *counter + 1
	}
}

func part2(password string, rule Rule, counter *int) {
	if (password[rule.num1] == rule.char[0]) !=
		(password[rule.num2] == rule.char[0]) {
		*counter = *counter + 1
	}
}
