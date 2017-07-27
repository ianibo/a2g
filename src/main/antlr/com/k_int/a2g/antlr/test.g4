grammar test;

@header { 
  package com.k_int.a2g.antler;
}

r   : 'hello' ID;
ID  : [a-z]+ ;
WS  : [ \t\r\n]+ -> skip ;
