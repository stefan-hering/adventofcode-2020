
val doorPubKey = 10441485L
val cardPubKey = 1004920L

fun part1() {
    val cardLoopSize = findLoopSize(cardPubKey, 7L)
    val doorLoopSize = findLoopSize(doorPubKey, 7L)

    println(calculate(cardLoopSize, doorPubKey))
    println(calculate(doorLoopSize, cardPubKey))
}

fun findLoopSize(key: Long, subject: Long): Int {
    var loopSize = 1

    var value = subject
    while(key != value) {
        loopSize++
        value *= subject
        value %= 20201227L
    }

    return loopSize
}

fun calculate(loopSize: Int, subject: Long): Long {
    var value = subject
    for (i in 1..(loopSize - 1)) {
        value *= subject
        value %= 20201227L
    }
    return value
}

fun main() {
    part1()
}

