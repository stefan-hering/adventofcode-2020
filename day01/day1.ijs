#!/Applications/j901/bin/jconsole

readfile =: 1!:1
numbers =. }:  0 ". toJ readfile < 'input'

part1 =. */ (>./ ~. (2020 = (] +/ ]) numbers)) # numbers -. 0
part2 =. */ (>./ >./ ~. ( 2020 = (] +/ ] +/ ]) numbers)) # numbers -. 0

echo part1
echo part2
exit ''
