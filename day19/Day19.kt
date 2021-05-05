sealed class Rule

class ReferencingRule(
    val rules: List<List<Int>>
) : Rule()

class ReferencingRuleTreeNode(
    var rules: List<List<Rule>>
) : Rule()

class CharacterRule(
    val character: Char
) : Rule()

val input = Unit.javaClass.getResource("/input").readText()
    .split("\n\n")
    .let { parseRules(it[0]) to parseMessages(it[1]) }

fun parseRules(rules: String) = rules.split("\n")
    .map { it.split(":") }
    .map { it[0].trim() to it[1].trim() }
    .map {
        if (it.second[0] == '"') {
            it.first.toInt() to CharacterRule(it.second[1])
        } else {
            it.first.toInt() to ReferencingRule(it.second.split("|")
                .map { it.trim() }
                .map { it.split(" ").map { it.toInt() } })
        }
    }
    .toMap()


fun parseMessages(messages: String) =
    messages.split("\n")

/*
 * Recursive descent parser approach
 */
fun buildSyntaxTree(rules: Map<Int, Rule>) =
    buildTreeNode(rules.get(0)!!, rules, 0)

fun buildTreeNode(
    current: Rule,
    rules: Map<Int, Rule>,
    index: Int,
    parents: MutableMap<Int, ReferencingRuleTreeNode> = mutableMapOf()
): Rule {
    return when (current) {
        is ReferencingRule -> {
            if (parents.contains(index)) {
                parents.get(index)!!
            } else {
                val newRule = ReferencingRuleTreeNode(listOf())
                parents.put(index, newRule)
                newRule.rules = current.rules.map { childrenLists ->
                    childrenLists.map { child ->
                        buildTreeNode(rules[child]!!, rules, child, parents)
                    }
                }
                newRule
            }
        }
        is CharacterRule -> {
            current
        }
        else -> throw IllegalStateException()
    }
}

class Scanner(val string: String, val index: Int = 0) {
    fun peek() = if (string.length > index) string[index] else null
    fun pop(steps: Int = 1): Scanner = Scanner(string, index + steps)
}

fun matchesRules(s: String, root: Rule): Boolean =
    descend(Scanner(s), root).any { it == s.length }

fun descend(scanner: Scanner, rule: Rule): List<Int> =
    when (rule) {
        is CharacterRule -> {
            if (rule.character == scanner.peek()) listOf(1) else listOf()
        }
        is ReferencingRuleTreeNode -> {
            rule.rules.flatMap { rules ->
                rules.fold(listOf(0)) { pops, rule ->
                    pops.flatMap { pop ->
                        descend(scanner.pop(pop), rule)
                            .map { it + pop }
                    }.distinct()
                }
            }.distinct()
        }
        else -> throw IllegalStateException()
    }


/*
 * Regex approach, only possible for part 1
 */
fun getRegex(rules: Map<Int, Rule>) =
    Regex(getRegex(rules[0]!!, rules)
        .replace(" ", "")
        .also { println(it) })

fun getRegex(current: Rule, rules: Map<Int, Rule>) =
    when (current) {
        is ReferencingRule -> {
            """(${
                current.rules.map { childrenLists ->
                    childrenLists.map { child -> getRegex(rules[child]!!, rules) }.joinToString(separator = "")
                }.joinToString(separator = "|")
            })"""
        }
        is CharacterRule -> {
            "${current.character}"
        }
        else -> throw IllegalStateException()

    }


fun part1(rules: Map<Int, Rule>, messages: List<String>) {
    val regex = getRegex(rules)

    println(messages.filter {
        regex.matchEntire(it) != null
    }.size)
}

fun part2(rules: Map<Int, Rule>, messages: List<String>) {
    val part2Rules = rules + mapOf(
        8 to ReferencingRule(listOf(listOf(42), listOf(42, 8))),
        11 to ReferencingRule(listOf(listOf(42, 31), listOf(42, 11, 31))),
    )

    val rootRule = buildSyntaxTree(part2Rules)
    messages.map {
        matchesRules(it, rootRule)
    }
        .filter { it }
        .count()
        .let { println(it) }
}

fun main() {
    part1(input.first, input.second)
    part2(input.first, input.second)
}
