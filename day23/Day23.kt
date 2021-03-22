val input = listOf(9, 1, 6, 4, 3, 8, 2, 7, 5)

class Cup (
    val number: Int,
) {
    lateinit var next:Cup
    lateinit var prev:Cup

    override fun equals(other: Any?): Boolean {
        return if(other is Cup) {
            number == other.number
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return number
    }
}

fun Cup.moveThreeto(target: Cup) {
    val third = this.next.next

    this.prev.next = third.next
    third.next.prev = this.prev

    val next = target.next
    target.next = this
    this.prev = target
    next.prev = third
    third.next = next
}

fun move(current:Cup, cups: Map<Int, Cup>): Cup {
    val taken = listOf(current.next, current.next.next, current.next.next.next)
    val takenNumbers = taken.map { it.number }.toSet()

    var destination = if (current.number - 1 == 0) cups.size else current.number - 1
    while (takenNumbers.contains(destination)) {
        if (--destination == 0) {
            destination = cups.size
        }
    }

    val target = cups[destination] ?: throw Exception("Cup $destination does not exist")

    current.next.moveThreeto(target)
    return current.next
}

fun createCups(numbers: List<Int>): Map<Int, Cup> {
    val cups = numbers.map{ Cup(it) }
    for (i in numbers.indices) {
        cups[i].next = cups[(i+1) % numbers.size]
        cups[i].prev = if(i-1 < 0) cups.last() else cups[i-1]
    }

    return cups.associateBy { it.number }
}

fun part1(cup: Cup) {
    var currentCup = cup.next
    var result = ""
    while(currentCup != cup) {
        result += currentCup.number
        currentCup = currentCup.next
    }

    println(result)
}

fun part2(cup: Cup) {
    println(cup.next.number.toLong() * cup.next.next.number.toLong())
}

fun main() {
    val cups = createCups(input)

    (1..100).fold(cups[input[0]]!!) { current, i ->
        move(current, cups)
    }
    part1(cups[1]!!)

    val part2input = input + (10..1000000)
    val part2Cups = createCups(part2input)

    (1..10000000).fold(part2Cups[input[0]]!!) { current, i ->
        move(current, part2Cups)
    }
    part2(part2Cups[1]!!)
}
