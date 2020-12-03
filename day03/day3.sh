#!/bin/bash

result=1

function countTrees {
  x=0
  y=-1
  trees=0

  while read line; do
    ((y=y+1))
    if [ $((y%$2)) -ne 0 ]; then
      continue
    fi

    if [ ${line:x%31:1} == "#" ];then
      ((trees=trees+1))
    fi
    ((x=x+$1))
  done

  echo $trees
  ((result=result*trees))
}

countTrees 1 1 < input
countTrees 3 1 < input
countTrees 5 1 < input
countTrees 7 1 < input
countTrees 1 2 < input
echo $result
