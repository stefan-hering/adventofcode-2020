fun readCoordinates(dimensions: Int): Set<Coordinate> {
    val initialState = mutableSetOf<List<Int>>()

    Unit.javaClass.getResource("/input").readText()
        .lines()
        .asSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .forEachIndexed { i, string ->
            string.toCharArray().forEachIndexed { j, char ->
                if (char == '#') {
                    initialState.add(listOf(i, j))
                }
            }
        }

    val emptyCoordinates = mutableListOf<Int>()
    for(i in 3..dimensions) {
        emptyCoordinates.add(0)
    }

    return initialState
        .map { Coordinate(it + emptyCoordinates) }
        .toSet()
}

data class Coordinate(
    val coordinates: List<Int>
) {
    fun getNeighbors(): List<Coordinate> {
        val neighbors = coordinates.asSequence()
            .map {
                listOf(it - 1, it, it + 1)
            }
            .fold(listOf(listOf<Int>())) { acc, axis ->
                acc.flatMap { coordinate ->
                    axis.map { value ->
                        coordinate + value
                    }
                }
            }
            .map { Coordinate(it) }

        return neighbors - this
    }
}

fun bootProcess(initialState: Set<Coordinate>) {
    var activeCubes: Set<Coordinate> = initialState

    for (i in 1..6) {
        activeCubes = cycle(activeCubes)
    }

    println(activeCubes.size)
}

fun cycle(activeCubes: Set<Coordinate>): Set<Coordinate> =
    activeCubes.asSequence()
        .flatMap { it.getNeighbors() }
        .distinct()
        .map {
            it to it.getNeighbors()
                .filter { activeCubes.contains(it) }
                .count()
        }
        .filter {
            activeCubes.contains(it.first) && (it.second == 2 || it.second == 3) ||
                    it.second == 3
        }
        .map { it.first }
        .toSet()

fun main() {
    bootProcess(readCoordinates(3))
    bootProcess(readCoordinates(4))
}
