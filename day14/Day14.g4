grammar Day14;

NUMBER  : [0-9]+;
BITMASK : [01X]+;
WS      : [\p{White_Space}]+ ;

assignment : 'mem' index WS '=' WS value WS;
value : NUMBER;
index : '[' NUMBER ']';

mask : 'mask' WS '=' WS BITMASK WS;

program : mask{1} assignment+;

programs : program+;
