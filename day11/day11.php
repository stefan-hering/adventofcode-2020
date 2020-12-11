<!doctype html>
<html>
<style>
.chair--taken {
    color:darkred;
}
.chair--free {
    color:darkgreen;
}
</style>
<body>
<div style="font-family: monospace">
<?php

const UP_LEFT = 0;
const UP = 1;
const UP_RIGHT = 2;
const LEFT = 3;
const RIGHT = 4;
const DOWN_LEFT = 5;
const DOWN = 6;
const DOWN_RIGHT = 7;

const DIRECTIONS = [UP_LEFT, UP, UP_RIGHT, LEFT, RIGHT, DOWN_LEFT, DOWN, DOWN_RIGHT];

function printChairs($seats) {
    global $x, $y;

    for ($i = 0; $i < $x; $i++) {
        echo "<div>";
        for ($j = 0; $j < $y; $j++) {
            $className = "floor";
            if($seats[$i][$j] == 'L') {
                $className = "chair--free";
            } else if($seats[$i][$j] == '#') {
                $className =  "chair--taken";
            }
            echo "<span class='" . $className . "'>";
            echo $seats[$i][$j];
            echo "</span>";
        }
        echo "</div>";
    }
}


function countTakenChairs($seats, $i, $j, $maxLength) {
    global $x, $y;

    $taken = 0;

    foreach (DIRECTIONS as $direction) {
        $position = 1;

        $done = false;
        while(! $done) {
            $ic = $i;
            $jc = $j;
            switch($direction) {
                case UP_LEFT:
                    $ic = $i - $position;
                    $jc = $j - $position;
                    break;
                case UP:
                    $ic = $i - $position;
                    break;
                case UP_RIGHT:
                    $ic = $i - $position;
                    $jc = $j + $position;
                    break;
                case LEFT:
                    $jc = $j - $position;
                    break;
                case RIGHT:
                    $jc = $j + $position;
                    break;
                case DOWN_LEFT:
                    $ic = $i + $position;
                    $jc = $j - $position;
                    break;
                case DOWN:
                    $ic = $i + $position;
                    break;
                case DOWN_RIGHT:
                    $ic = $i + $position;
                    $jc = $j + $position;
                    break;
            }

            if($ic < 0 || $ic > $x - 1 || $jc < 0 || $jc > $y - 1) {
                $done = true;
            } else if ($seats[$ic][$jc] == '#') {
                $taken++;
                $done = true;
            } else if ($seats[$ic][$jc] == 'L') {
                $done = true;
            } else {
                $position++;
            }
            if($position > $maxLength) {
                $done = true;
            }
        }

    }
    return $taken;
}

function calculateTakenSeats($seats, $tolerance, $maxRange) {
    global $x, $y;
    $changed = true;

    while($changed) {
        $changed = false;

        $next = array_merge(array(), $seats);
        for ($i = 0; $i < $x; $i++) {
            for ($j = 0; $j < $y; $j++) {
                if ($seats[$i][$j] == '.') {
                    continue;
                }

                $count = countTakenChairs($seats,$i, $j, $maxRange);
                if ($seats[$i][$j] == 'L' && $count == 0) {
                    $next[$i][$j] = '#';
                    $changed = true;
                } else if ($seats[$i][$j] == '#' && $count >= $tolerance) {
                    $next[$i][$j] = 'L';
                    $changed = true;
                }
            }
        }
        $seats = $next;
    }

    return $seats;
}

function countResult($chairs){
    global $x, $y;

    $result = 0;
    for ($i = 0; $i < $x; $i++) {
        for ($j = 0; $j < $y; $j++) {
            if ($chairs[$i][$j] == '#') {
                $result++;
            }
        }
    }

    return $result;
}

$seats = file("input", FILE_IGNORE_NEW_LINES);

$x = count($seats);
$y = strlen($seats[0]);

$part1 = calculateTakenSeats(array_merge(array(), $seats), 4, 1);
$part2 =  calculateTakenSeats(array_merge(array(), $seats), 5, PHP_INT_MAX);


printChairs($part1);
echo "<p>" . countResult($part1) . "</p>";
echo "<br>";
printChairs($part2);
echo "<p>" . countResult($part2) . "</p>";
?>
</div>
</body>
</html>
