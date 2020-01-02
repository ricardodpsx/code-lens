grammar AstQ;
setQuery: 'SET' subQuery paramSet (',' paramSet)*;
paramSet: reference '=' subQuery;
query: type+ ('|' aggregator)?;
aggregator: func;
type: PREFIX? reference attribute? relation?;
attribute: BRK_OP expr BRK_CL;
relation: '>';
expr:
    	literal
    |   subQuery
    |   reference
    |   func
    |	'(' expr ')'
    |   expr op0 expr
    |   expr op1 expr
    |   expr op2 expr
    |   expr op3 expr;
func: reference ('(' expr + (',' expr)* ')')?;
subQuery: '{' query '}';
literal: INT | STRING;
INT     : [0-9]+ ;
op0: ( '*' | '/');
op1: ( '+' | '-' );
op2: ( '^=' | '*=' | '$=' | '<' | '>' | '>=' | '<=' | '!=' | '+' | '-' | '*' | '/' | '=');
op3: ( '&&' | '||');
PREFIX: [#.];
reference:  NAME | '$' | '*';
NAME: [A-Za-z_+:] [A-Za-z0-9_]*;
STRING: SQSTRING | DQSTRING;
SQSTRING: '\'' ( '\\\'' | . )*? '\'';
DQSTRING:  '"' ( '\\"' | . )*? '"';
BRK_OP: '[';
BRK_CL: ']';
WHITESPACE : [ \n\t\r]+ -> skip ;
