import java.util.regex.Pattern

data class GameState(val player1: List<Int>, val player2: List<Int>)

fun readDecks(): GameState =
    Unit.javaClass.getResource("/input")
        .readText()
        .split(Pattern.compile("Player [12]:"))
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map {
            it.split("\n").mapNotNull { it.toIntOrNull() }
        }
        .map { it.toMutableList() }
        .take(2).let {
            return GameState(it[0], it[1])
        }

fun round(game: GameState):GameState {
    val card1 = game.player1.first()
    val card2 = game.player2.first()

    return if (card1 > card2) {
        game.copy(
            player1 = game.player1.drop(1) + listOf(card1, card2),
            player2 = game.player2.drop(1)
        )
    } else {
        game.copy(
            player1 = game.player1.drop(1),
            player2 = game.player2.drop(1) + listOf(card2, card1)
        )
    }
}

fun score(cards: List<Int>): Int = cards.reversed()
    .mapIndexed { index, card ->
        (index + 1) * card
    }
    .sum()


fun play(game: GameState) {
    var state = game
    while (state.player1.isNotEmpty() && state.player2.isNotEmpty()) {
        state = round(state)
    }
    println(score(if (state.player1.size > 0) state.player1 else state.player2))
}

fun playRecursive(game: GameState, isMainGame: Boolean = true): Boolean {
    var state = game

    val previousGameStates = mutableSetOf<GameState>()
    while (state.player1.isNotEmpty() && state.player2.isNotEmpty() && previousGameStates.contains(state).not()) {
        previousGameStates.add(state)
        state = roundRecursive(state)
    }
    if(isMainGame) {
        println(score(if (state.player1.size > 0) state.player1 else state.player2))
    }
    return state.player1.size > 0
}

fun roundRecursive(game: GameState): GameState {
    val card1 = game.player1.first()
    val card2 = game.player2.first()

    val player1Win = if(game.player1.size > card1 && game.player2.size > card2) {
        playRecursive(
            game.copy(
                player1 = game.player1.subList(1, 1 + card1),
                player2 = game.player2.subList(1, 1 + card2)
            ),
            false
        )
    } else card1 > card2

    return if(player1Win) {
        game.copy(
            player1 = game.player1.drop(1) + listOf(card1, card2),
            player2 = game.player2.drop(1)
        )
    } else {
        game.copy(
            player1 = game.player1.drop(1),
            player2 = game.player2.drop(1) + listOf(card2, card1)
        )
    }
}

fun main() {
    val initialState = readDecks()
    play(initialState)
    playRecursive(initialState)
}
