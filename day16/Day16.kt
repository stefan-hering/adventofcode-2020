fun readInput(): PuzzleInput {
    val lines = Unit.javaClass.getResource("/input").readText()
        .split("nearby tickets:", "your ticket:")
        .map { it.trim() }
        .filter { it.isNotBlank() }

    val fields = lines[0].lines()
        .map { it.split(":") }
        .map {
            Field(it[0], it[1]
                .split(" or ")
                .map { it.trim() }
                .map { ranges ->
                    ranges.split("-").let { range ->
                        Range(range[0].toInt(), range[1].toInt())
                    }
                })
        }

    val myTicket = Ticket(lines[1]
        .split(",")
        .map { it.toInt() }
    )
    val otherTickets = lines[2]
        .split("\n")
        .map {
            Ticket(
                it.split(",")
                    .map { it.toInt() }
            )
        }

    return PuzzleInput(fields, myTicket, otherTickets)
}

data class Field(val name: String, val validRanges: List<Range>)
data class Range(val min: Int, val max: Int)
data class Ticket(val fields: List<Int>)

data class PuzzleInput(
    val fields: List<Field>,
    val myTicket: Ticket,
    val nearbyTickets: List<Ticket>
)

fun part1(puzzleInput: PuzzleInput): Int {
    val validRanges = puzzleInput.fields.flatMap { it.validRanges }

    return puzzleInput.nearbyTickets
        .flatMap { it.fields }
        .filter { field ->
            validRanges.isValid(field).not()
        }
        .sum()
}

fun part2(puzzleInput: PuzzleInput): Long {
    val validRanges = puzzleInput.fields.flatMap { it.validRanges }

    val validTickets = puzzleInput.nearbyTickets
        .filter {
            it.fields.all { field ->
                validRanges.isValid(field)
            }
        }

    val takenFields = mutableSetOf<Int>()
    val indexToField = puzzleInput.fields
        .map { it to validTickets.findValidIndexes(it) }
        .sortedBy { it.second.size }
        .map {
            val leftFields = it.second.minus(takenFields)
            if (leftFields.size != 1) {
                throw Exception("Greedy approach didn't work")
            }

            takenFields.add(leftFields.first())
            leftFields.first() to it.first
        }

    return indexToField.filter {
        it.second.name.startsWith("departure")
    }.map {
        puzzleInput.myTicket.fields[it.first]
    }.map { it.toLong() }
        .reduce { acc, i -> acc * i }
}


fun List<Ticket>.findValidIndexes(field: Field): List<Int> = map { ticket ->
    ticket.fields.mapIndexed { i, value ->
        i to field.validRanges.isValid(value)
    }
}.flatten()
    .groupBy({ it.first }, { it.second })
    .mapValues { it.value.all { it } }
    .filter { it.value }
    .map { it.key }


fun List<Range>.isValid(number: Int) = any { range ->
    range.min <= number && range.max >= number
}

fun main() {
    val input = readInput()

    println(part1(input))
    println(part2(input))
}
