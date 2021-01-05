import kotlin.math.abs

fun readLines(): List<String> =
    Unit.javaClass.getResource("/input")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }

data class Action(val direction: Char, val distance: Int)

val directions = listOf('N', 'E', 'S', 'W')

data class Position(val x: Int, val y: Int, val facing: Int) {
    operator fun plus(other: Position): Position {
        return Position(
            x = x + other.x,
            y = y + other.y,
            facing = facing
        )
    }

    operator fun minus(other: Position): Position {
        return Position(
            x = x - other.x,
            y = y - other.y,
            facing = facing
        )
    }

    operator fun times(times: Int): Position {
        return Position(
            x = x * times,
            y = y * times,
            facing = facing
        )
    }
}
val zeroPosition = Position(0,0,1)

data class ShipWaypoint(val shipPosition: Position, val waypoint: Position)

fun parseAction(line: String): Action = Action(line[0], line.substring(1, line.length).toInt())

fun move(position: Position, action: Action): Position {
    return when (action.direction) {
        'N' -> position.copy(y = position.y - action.distance)
        'S' -> position.copy(y = position.y + action.distance)
        'E' -> position.copy(x = position.x + action.distance)
        'W' -> position.copy(x = position.x - action.distance)
        'L' -> position.copy(facing = (position.facing + action.distance / 30) % 4)
        'R' -> position.copy(facing = (position.facing + action.distance / 90) % 4)
        'F' -> move(position, action.copy(direction = directions[position.facing]))
        else -> throw Exception()
    }
}

fun part1(directions: List<Action>) {
    val finalPosition = directions.fold(
        zeroPosition, { position, action ->
            move(position, action)
        })

    println(abs(finalPosition.x) + abs(finalPosition.y))
}

fun moveToWaypoint(shipWaypoint: ShipWaypoint, action: Action): ShipWaypoint {
    return shipWaypoint.run {
        when (action.direction) {
            'N' -> copy(waypoint = waypoint.copy(y = waypoint.y - action.distance))
            'S' -> copy(waypoint = waypoint.copy(y = waypoint.y + action.distance))
            'E' -> copy(waypoint = waypoint.copy(x = waypoint.x + action.distance))
            'W' -> copy(waypoint = waypoint.copy(x = waypoint.x - action.distance))
            'L', 'R' -> rotateWayPoint(shipWaypoint, action)
            'F' -> copy(shipPosition = shipPosition + (waypoint * action.distance))
            else -> throw Exception()
        }
    }
}

fun rotateWayPoint(shipWaypoint: ShipWaypoint, action: Action): ShipWaypoint {
    return shipWaypoint.run {
        return if (action.distance == 180) {
            copy(waypoint = waypoint * (-1))
        } else if (action.direction == 'L' && action.distance == 90 || action.direction == 'R' && action.distance == 270) {
            copy(waypoint = waypoint.copy(x = waypoint.y, y = waypoint.x * (-1)))
        } else {
            copy(waypoint = waypoint.copy(x = waypoint.y * (-1), y = waypoint.x))
        }
    }
}

fun part2(directions: List<Action>) {
    val finalPosition = directions.fold(
        ShipWaypoint(
            shipPosition = zeroPosition,
            waypoint = Position(10, -1, 1)
        ),
        { position, action ->
            moveToWaypoint(position, action)
        })

    println(abs(finalPosition.shipPosition.x) + abs(finalPosition.shipPosition.y))
}

fun main() {
    val directions = readLines()
        .map { parseAction(it) }

    part1(directions)
    part2(directions)
}
