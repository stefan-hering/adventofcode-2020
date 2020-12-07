grammar day7;

NUMBER   : [0-9] ;
BAGS     : 'bag' 's'* ;
CONTAIN  : 'contain' ;
WORD     : [a-z]+ ;
WS       : [\p{White_Space}]+ ;

sentences : (sentence '\n')* ;

sentence : color WS BAGS WS 'contain' WS rules ;

rules : containsRules '.'
       | emptyRule '.';

containsRules : (containsRule ', '*)* ;

containsRule : NUMBER WS color WS BAGS;

emptyRule : 'no other' WS BAGS;

color : WORD WS WORD ;

