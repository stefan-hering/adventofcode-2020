val input = listOf(9, 1, 6, 4, 3, 8, 2, 7, 5)
val input2 = listOf(3, 8, 9, 1, 2, 5, 4, 6, 7)

fun move(currentIndex: Int, cups: List<Int>): Pair<Int, List<Int>> {
    val taken = listOf(1, 2, 3).map { cups[(currentIndex + it) % cups.size] }

    val rest = cups.filterNot { taken.contains(it) }

    var destination = if (cups[currentIndex] - 1 == 0) cups.size - 1 else cups[currentIndex] - 1
    while (taken.contains(destination)) {
        if (--destination == 0) {
            destination = cups.size - 1
        }
    }

    destination = rest.indexOf(destination)

    val targetList = rest.subList(0, destination + 1) + taken + rest.subList(destination + 1, rest.size)

    return (currentIndex + 1) % cups.size - 1 to targetList.indexOf(cups[currentIndex]).let {
        if (it == currentIndex) {
            targetList
        } else {
            targetList.drop(it - currentIndex) + targetList.take(it - currentIndex)
        }
    }
}

fun part1(cups: List<Int>) {
    val index = cups.indexOf(1)

    println((cups.subList(index + 1, cups.size) + cups.subList(0, index))
        .map { it.toString() }
        .joinToString()
        .replace(", ", ""))
}

fun main() {
    val result = (1..100).fold(0 to input) { current, i ->
        move(current.first, current.second)
    }
    part1(result.second)
}
