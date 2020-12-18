#!/Applications/j901/bin/jconsole

readfile =: 1!:1
numbers =. > 0 ". each cutopen toJ readfile < 'input'

NB. Let's just assume the idiomatic way of writing J is to cram as many symbols into one line as possible

part1 =. 25 { 1&|.^:(>./^:2&((-. = i.25) * (25 { ]) = 25 {. ] +/ 25 {. ])&])^:_ numbers

len =. 1&+^:((1 - (>./)&(part1 = (] +/\&numbers)))&])^:_ (2)
part2 =. ((>./&]) + (<./&])) (len {. ((part1 = len +/\ numbers) i. 1) |. numbers)

echo part1
echo part2



NB. Everything again in steps in steps, part 1:
NB. create a table of which of the first 25 numbers sum up to the number at index 25
allsums =. (25 { ]) = 25 {. ] +/ 25 {. ]

NB. filter out the identity matrix (same number twice)
sumsmatrix =. (-. = i.25) * allsums

NB. max of the table, 1 if number matches, 0 if not -> we need to find the spot where this is 0
isvalid =. >./^:2&sumsmatrix

NB. next result (array shifted left)
shift =. 1&|.

NB. loop over it, runs isvalid returns 0, then take the element at index 25, which is our target
part1 =. 25 { shift^:(isvalid&])^:_ numbers

NB. Part 2:
NB. sum all numbers with length of the parameter to this
subsums =. +/\&numbers

NB. check if any sum of the parameter (]) sums up to the solution from part 1. Use "1 -" so this can be our loop condition
iscorrect =. 1 - >./&(part1 = (] subsums))

NB. check sublengths sequentially to see how long the range needs to be
part2length =.  1&+^:(iscorrect&])^:_ (2)

NB. with the correct sublength, find the position again where the array starts
part2start =. (part1 = part2length +/\ numbers) i. 1

NB. select the subarray to get the range that adds up to part 1
part2range =. (part2length {. part2start |. numbers)

NB. add min and max together
part2 =. ((>./&]) + (<./&])) part2range

echo part1
echo part2

exit ''
