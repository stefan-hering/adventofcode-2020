#!/Applications/j901/bin/jconsole

readfile =: 1!:1
file =. readfile < 'input'

numberString =. toJ file
numbers =. }:  0 ". numberString

NB. find numbers that sum to 2020 by creating a table with all possible additions
numbertable =. (] +/ ]) numbers
NB. then get the indexes on that table that sum to 2020
indexes =. 2020 = numbertable

NB. get max of each table column to get a list of indexes
indexes =. >./ indexes

NB. set numbers in the list to 0 or number by repeating 0 or 1 times
numbersf =. indexes # numbers

NB. filter 0s out of the list (.,) and multiply everything else together
result1 =. */ numbersf -. 0

NB. day 2 works similarly, just with another dimension to add up 3 unmbers ( ] is repeat )
indexes2 =. 2020 = (] +/ ] +/ ]) numbers
NB. this is the same as day 1, except it reduces one extra dimension
result2 =. */ ((>./ >./ indexes2)) # numbers -. 0

echo result1
echo result2
exit ''
