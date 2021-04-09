fun readInstructions(): List<String> =
    Unit.javaClass.getResource("/testinput").readText()
        .lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }

enum class Direction {
    W, E, NW, NE, SW, SE
}

// hexagons represented with cube coordinates: https://www.redblobgames.com/grids/hexagons/
data class TileIndex(
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0
) {
    fun move(direction: Direction): TileIndex =
        when (direction) {
            Direction.W -> copy(x = x - 1, y = y + 1)
            Direction.E -> copy(x = x + 1, y = y - 1)
            Direction.NW -> copy(y = y + 1, z = z - 1)
            Direction.SE -> copy(y = y - 1, z = z + 1)
            Direction.NE -> copy(x = x + 1, z = z - 1)
            Direction.SW -> copy(x = x - 1, z = z + 1)
        }
}

fun findIndex(directions: String): TileIndex {
    val parsedDirections = directions
        .replace("se", "2")
        .replace("sw", "3")
        .replace("nw", "5")
        .replace("ne", "6")
        .replace("e", "1")
        .replace("w", "4")

    return parsedDirections.toCharArray()
        .fold(TileIndex(), { index, c ->
            when (c) {
                '1' -> index.move(Direction.E)
                '2' -> index.move(Direction.SE)
                '3' -> index.move(Direction.SW)
                '4' -> index.move(Direction.W)
                '5' -> index.move(Direction.NW)
                '6' -> index.move(Direction.NE)
                else -> throw Exception("Unknown character: $c. $directions, $parsedDirections")
            }
        })
}

fun part1(instructions: List<String>): Map<TileIndex, Boolean> {
    val tiles: MutableMap<TileIndex, Boolean> = mutableMapOf()

    instructions.forEach {
        val index = findIndex(it)

        if (tiles.contains(index)) {
            tiles[index] = tiles[index]!!.not()
        } else {
            tiles[index] = true
        }
    }

    println(tiles.countBlackTiles())

    return tiles
}

fun part2(initialTiles: Map<TileIndex, Boolean>) {

    var tiles = initialTiles
    var nextTiles: MutableMap<TileIndex, Boolean> = mutableMapOf()

    val doFlip = { tile: TileIndex ->
        val blackNeighbors = tiles.countBlackNeighbors(tile)

        if (tiles.isBlack(tile)) {
            nextTiles[tile] = !(blackNeighbors == 0 || blackNeighbors > 2)
        } else {
            nextTiles[tile] = blackNeighbors == 2
        }
    }

    for (i in 1..100) {
        nextTiles = tiles.mapKeys { it.key.copy() }.toMutableMap()

        tiles.keys.forEach {
            doFlip(it)
        }

        tiles.entries
            .asSequence()
            .filter { it.value }
            .map { it.key }
            .flatMap { tile ->
                Direction.values().map { direction -> tile.move(direction) }
            }
            .filter { tiles.contains(it).not() }
            .forEach {
                doFlip(it)
            }

        tiles = nextTiles
    }

    println(tiles.countBlackTiles())
}


fun Map<TileIndex, Boolean>.countBlackNeighbors(tile: TileIndex) = Direction.values().fold(0) { acc, direction ->
    acc + if (isBlack(tile.move(direction))) 1 else 0
}

fun Map<TileIndex, Boolean>.isBlack(index: TileIndex) = this[index] ?: false

fun Map<TileIndex, Boolean>.countBlackTiles() = values.filter { it }.count()

fun main() {
    val instructions = readInstructions()

    val tiles = part1(instructions)
    part2(tiles)
}
