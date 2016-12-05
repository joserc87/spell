grammar Spell;

@header {
package nl.thedocumentwizard.spell.parser;
}

@lexer::members {
  boolean atStartOfInput() {
    return super.getCharPositionInLine() == 0 && super.getLine() == 1;
  }
}

// A wizard is not more that a set of steps
wizard: (NEWLINE | step)* EOF;

// Each step is a step header + a block
step
  : STEP STRING (COMMA STRING)? COLON NEWLINE
  ;

//////////////////
// LEXER RULES: //
//////////////////

// From python:
// https://github.com/antlr/grammars-v4/blob/master/python3/Python3.g4

STRING
 : '\'' ( STRING_ESCAPE_SEQ | ~[\\\r\n'] )* '\''
 | '"' ( STRING_ESCAPE_SEQ | ~[\\\r\n"] )* '"'
 ;

/// stringescapeseq ::=  "\" <any source character>
fragment STRING_ESCAPE_SEQ
 : '\\' .
 ;

NEWLINE
 : ( {atStartOfInput()}?   SPACES
   | ( '\r'? '\n' | '\r' ) SPACES?
   )
 ;

SKIP_
 : ( SPACES | COMMENT | LINE_JOINING ) -> skip
 ;

fragment SPACES
 : [ \t]+
 ;

fragment COMMENT
 : '#' ~[\r\n]*
 ;

fragment LINE_JOINING
 : '\\' SPACES? ( '\r'? '\n' | '\r' )
 ;

STEP    : 'step';
COLON   : ':';
COMMA   : ',';
INT     : [0-9]+ ;
