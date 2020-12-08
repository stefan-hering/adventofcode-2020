grammar day8;

KEYWORD  : ('acc'|'jmp'|'nop') ;
NUMBER   : [+-][0-9]* ;
WS       : [\p{White_Space}]+ ;

commands : (command '\n')* ;

command : KEYWORD WS NUMBER ;

