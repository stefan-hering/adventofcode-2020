import groovy.transform.ToString

String readFile() {
    return new String({}.class
            .getResourceAsStream("./input")
            .readAllBytes())
}

@ToString
class Tile {
    Long tileNumber
    List<List<Character>> tiles
}

Tile parseTile(String tile) {
    def split = tile.split(":\n")
    return new Tile(
            tileNumber: Integer.valueOf(split[0].find(~/[0-9]+/)),
            tiles: split[1].split("\n").collect { it.chars.toList() }
    )
}

List<Tile> parseTiles(String tiles) {
    return tiles.split("\n\n").toList()
            .collect { parseTile(it) }
}

List<List<Character>> flipX(List<List<Character>> tiles) {
    return tiles.reverse()
}

List<List<Character>> flipY(List<List<Character>> tiles) {
    return tiles.collect { it.reverse() }
}

List<List<Character>> transpose(List<List<Character>> tiles) {
    return GroovyCollections.transpose(tiles)
}

Tile transform(Tile tile, Closure transformation) {
    return new Tile(
            tileNumber: tile.tileNumber,
            tiles: transformation(tile.tiles)
    )
}

List<Tile> combinations(Tile tile) {
    return [
            tile,
            transform(tile, { flipX(it) }),
            transform(tile, { flipY(it) }),
            transform(tile, { flipX(flipY(it)) }),
            transform(tile, { transpose(it) }),
            transform(tile, { transpose(flipX(it)) }),
            transform(tile, { transpose(flipY(it)) }),
            transform(tile, { transpose(flipX(flipY((it)))) })
    ]
}

enum Direction {
    ABOVE, BELOW, LEFT, RIGHT
}

def checkSides(Tile a, Tile b, Direction side) {
    int i = side == Direction.LEFT ? 0 : 9

    def fits = true;
    for (j in 0..9) {
        if (a.tiles[j][i] != b.tiles[j][9 - i]) {
            fits = false;
        }
    }

    return fits
}

// brute force all combinations for two tiles (4*8 checks)
Tuple2<Direction, Tile> fits(Tile a, Tile b) {
    def result = null
    combinations(b).any { transformedTile ->
        if (a.tiles[0] == transformedTile.tiles.last()) {
            result = new Tuple2(Direction.ABOVE, transformedTile)
            true
        } else if (a.tiles.last() == transformedTile.tiles[0]) {
            result = new Tuple2(Direction.BELOW, transformedTile)
            true
        } else if (checkSides(a, transformedTile, Direction.LEFT)) {
            result = new Tuple2(Direction.LEFT, transformedTile)
            true
        } else if (checkSides(a, transformedTile, Direction.RIGHT)) {
            result = new Tuple2(Direction.RIGHT, transformedTile)
            true
        } else {
            false
        }
    }

    return result
}

def assemble(List<Tile> tiles) {
    Tile[][] assembledTiles = new Tile[31][31]
    def placedTiles = []

    while (tiles.size() > 0) {
        tileLoop:
        for (tile in tiles) {
            if (placedTiles.size() == 0) {
                assembledTiles[10][10] = tile
                placedTiles.add(tile)
            } else {
                for (i in 0..20) {
                    for (j in 0..20) {
                        if (assembledTiles[i][j] == null) {
                            continue
                        }

                        def fit = fits(assembledTiles[i][j], tile)
                        if (fit != null) {
                            switch (fit.v1) {
                                case Direction.LEFT:
                                    assembledTiles[i][j - 1] = fit.v2
                                    placedTiles.add(tile)
                                    break
                                case Direction.RIGHT:
                                    assembledTiles[i][j + 1] = fit.v2
                                    placedTiles.add(tile)
                                    break
                                case Direction.ABOVE:
                                    assembledTiles[i - 1][j] = fit.v2
                                    placedTiles.add(tile)
                                    break
                                case Direction.BELOW:
                                    assembledTiles[i + 1][j] = fit.v2
                                    placedTiles.add(tile)
                                    break
                            }
                            continue tileLoop
                        }
                    }
                }
            }
        }

        tiles = tiles - placedTiles
    }

    return assembledTiles.collect { tileArray ->
        tileArray.findAll { tile ->
            tile != null
        }.asList()
    }.findAll {
        it.size() > 0
    }
}

def part1(List<List<Tile>> tiles) {
    println(tiles.first().first().tileNumber *
            tiles.first().last().tileNumber *
            tiles.last().first().tileNumber *
            tiles.last().last().tileNumber)
}

def createMap(List<List<Tile>> tiles) {
    List<List<Character>> map = new ArrayList<>()
    int size = tiles[0][0].tiles[0].size()

    for (tileList in tiles) {
        List<List<Character>> rows = new ArrayList<>();
        for (i in 0..7) {
            rows.add(new ArrayList())
        }
        for (tile in tileList) {
            for (i in 1..8) {
                rows[i - 1].addAll(tile.tiles[i].subList(1, size - 1))
            }
        }
        map.addAll(rows)
    }

    return map
}

def findSeaMonsters(List<List<Character>> map) {
    def mapStrings = map.collect {
        it.join("")
    }

    def line1 = ~/(.{18})#(.)/
    def line2 = ~/#(.{4})##(.{4})##(.{4})###/
    def line3 = ~/(.)#(.{2})#(.{2})#(.{2})#(.{2})#(.{2})#(.{3})/

    for (i in 0..map.size() - 3) {
        def matcher = line1.matcher(mapStrings[i])

        int start = 0
        while (matcher.find(start)) {
            start = matcher.start()
            int end = matcher.end()

            if (mapStrings[i + 1].substring(start, end).matches(line2) &&
                    mapStrings[i + 2].substring(start, end).matches(line3)) {

                mapStrings[i] = mapStrings[i].substring(0, start) +
                        mapStrings[i].substring(start, end).replaceFirst(line1, '$1O$2') +
                        mapStrings[i].substring(end, mapStrings[i].size())
                mapStrings[i + 1] = mapStrings[i + 1].substring(0, start) +
                        mapStrings[i + 1].substring(start, end).replaceFirst(line2, 'O$1OO$2OO$3OOO') +
                        mapStrings[i + 1].substring(end, mapStrings[i + 1].size())
                mapStrings[i + 2] = mapStrings[i + 2].substring(0, start) +
                        mapStrings[i + 2].substring(start, end).replaceFirst(line3, '$1O$2O$3O$4O$5O$6O$7') +
                        mapStrings[i + 2].substring(end, mapStrings[i + 2].size())
            }

            start++
        }
    }

    def finishedMap = mapStrings.join("\n")
    if (finishedMap.count("O") > 0) {
        println(finishedMap.count("#"))
        System.exit(0)
    }
}

def assembledTiles = assemble(parseTiles(readFile()))
part1(assembledTiles)
def assembledMap = createMap(assembledTiles)

combinations(new Tile(
        tileNumber: 0,
        tiles: assembledMap
)).forEach {
    findSeaMonsters(it.tiles)
}
