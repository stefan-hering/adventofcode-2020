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
		line := scanner.Text()

		split := strings.Split(line, ":")
		rule := strings.Split(split[0], " ")
		counts := strings.Split(rule[0], "-")
		min, _ := strconv.Atoi(counts[0])
		max, _ := strconv.Atoi(counts[1])

		r := Rule{min, max, rule[1]}

		check1(split[1], r, &counter1)
		check2(split[1], r, &counter2)
	}

	log.Print(counter1)
	log.Print(counter2)
}

func check1(password string, rule Rule, counter *int) {
	charCount := strings.Count(password, rule.char)

	if charCount >= rule.num1 && charCount <= rule.num2 {
		*counter = *counter + 1
	}
}

func check2(password string, rule Rule, counter *int) {
	if (password[rule.num1] == rule.char[0]) !=
		(password[rule.num2] == rule.char[0]) {
		*counter = *counter + 1
	}
}
