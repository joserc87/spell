grammar Spell;

@header {
package nl.thedocumentwizard.spell.parser.antlr;
}

// A wizard is not more that a set of steps
wizard: (NEWLINE | step)* EOF;

// Each step is a step header + a block
step: 'step ' STRING COLON NEWLINE ;


// TOKENS:
// Reserver words
STEP    : 'step';

// From python:
// https://github.com/antlr/grammars-v4/blob/master/python3/Python3.g4

/// shortstring     ::=  "'" shortstringitem* "'" | '"' shortstringitem* '"'
/// shortstringitem ::=  shortstringchar | stringescapeseq
/// shortstringchar ::=  <any source character except "\" or newline or the quote>
STRING
 : '\'' ( STRING_ESCAPE_SEQ | ~[\\\r\n'] )* '\''
 | '"' ( STRING_ESCAPE_SEQ | ~[\\\r\n"] )* '"'
 ;

/// stringescapeseq ::=  "\" <any source character>
fragment STRING_ESCAPE_SEQ
 : '\\' .
 ;

COLON   : ':';
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;
