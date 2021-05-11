val input = Unit.javaClass.getResource("/input").readText()
    .lines()
    .filter { it.isNotBlank() }

val plus = Regex("(\\d+)\\+(\\d+)") to { result: MatchResult ->
    "${result.groupValues[1].toLong() + result.groupValues[2].toLong()}"
}
val times = Regex("(\\d+)\\*(\\d+)") to { result: MatchResult ->
    "${result.groupValues[1].toLong() * result.groupValues[2].toLong()}"
}
val timesBracket = Regex("\\((\\d+(?:\\*\\d+)+)\\)") to { result: MatchResult ->
    result.groupValues[1].split("*").fold(1L) { acc, i ->
        acc * i.toLong()
    }.toString()
}
val brackets = Regex("\\((\\d+)\\)") to { result: MatchResult ->
    result.groupValues[1]
}
val rules: List<Pair<Regex, (MatchResult) -> String>> = listOf(
    brackets,
    timesBracket,
    plus,
    times
)

fun eval(formula: String): String {
    for(rule in rules) {
        val result = rule.first.find(formula)
        if(result != null) {
            return eval(formula.replaceRange(result.range, rule.second.invoke(result)))
        }
    }
    return formula
}

fun part2() = input.map {
    it.replace(" ", "")
}.map {
    eval(it).toLong().also { println(it) }
}.sum()

fun main() {
    println(part2())
}
