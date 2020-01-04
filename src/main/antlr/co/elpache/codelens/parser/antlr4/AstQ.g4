grammar AstQ;
setQuery: 'SET' subQuery paramSet (',' paramSet)*;
paramSet: reference '=' subQuery;
query: type+ ('|' aggregator)?;
aggregator: func;
type: PREFIX? reference attribute? relation?;
attribute: BRK_OP expr BRK_CL;
relation: directRelation | followRelation;
directRelation:  ('-' NAME)?'>';
followRelation: ('-' NAME)? '>>';
expr:
    	literal
    |   subQuery
    |   reference
    |   func
    |   group
    |   expr op0 expr
    |   expr op1 expr
    |   expr op2 expr
    |   expr op3 expr
    |   expr alias NAME;
group:  '(' expr ')';
func: reference '(' (expr + (',' expr)*)? ')';
subQuery: '{' query '}';
literal: INT | STRING | DECIMAL;
INT         : [\\-]?[0-9]+ ;
DECIMAL     : [\\-]?([0-9]+'.')?[0-9]+ ;
op0: ( '*' | '/');
op1: ( '+' | '-' );
op2: ( '^=' | '*=' | '$=' | '<' | '>' | '>=' | '<=' | '!=' | '+' | '-' | '*' | '/' | '=');
op3: ( '&&' | '||');
alias: 'as';
PREFIX: [#.];
reference:  NAME | '$' | '*';
NAME: [A-Za-z_+:] [A-Za-z0-9_]*;
STRING: SQSTRING | DQSTRING;
SQSTRING: '\'' ( '\\\'' | . )*? '\'';
DQSTRING:  '"' ( '\\"' | . )*? '"';
BRK_OP: '[';
BRK_CL: ']';
WHITESPACE : [ \n\t\r]+ -> skip ;
