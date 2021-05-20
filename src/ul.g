grammar ul;


@members
{
protected void mismatch (IntStream input, int ttype, BitSet follow)
        throws RecognitionException
{
        throw new MismatchedTokenException(ttype, input);
}
public Object recoverFromMismatchedSet (IntStream input,
                                      RecognitionException e,
                                      BitSet follow)
        throws RecognitionException
{
        reportError(e);
        throw e;
}
public Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
        throws RecognitionException
{
        throw new MismatchedTokenException(ttype, input);
}
}

@rulecatch {
        catch (RecognitionException ex) {
                reportError(ex);
                throw ex;
        }
}


program : function+ EOF
	;

function: functionDecl functionBody
	;

functionDecl: compoundType id '(' formalParameters? ')'
	;

formalParameters : compoundType id (',' compoundType id)*
        ;

functionBody: '{' varDecl* statement* '}'
	;

varDecl : compoundType id ';'
        ;

block   : '{' statement* '}'
        ;

/* Statements */

statement       : 
                statEmpty
                | statIf
                | statWhile
                | statPrint
                | statPrintln
                | statReturn
                | (id '=')=> statAssn
                | (arrayAccess '=')=> statArrAssn
                | statExpr
                ;

statEmpty       : ';'
                ;

statIf          : IF '(' expr ')' block (ELSE block)?
                ;

statWhile       : WHILE '(' expr ')' block
                ;

statPrint       : PRINT expr ';'
                ;

statPrintln     : PRINTLN expr ';'
                ;

statReturn      : RETURN expr? ';'
                ;

statArrAssn     : arrayAccess '=' expr ';'
                ;

statAssn        : id '=' expr ';'
                ;

statExpr        : expr ';'
                ;

/* Expressions */

expr            : exprEqualTo
	        ;

exprEqualTo     : exprLessThan ('==' exprLessThan)*
                ;

exprLessThan    : exprAddSub ('<' exprAddSub)*
                ;

exprAddSub      : exprMult (('+'|'-') exprMult)*
                ;

exprMult        : atom ('*' atom)*
	        ;

atom    :
        (id '[')=> arrayAccess
        | (id '(')=> call
        | id
        | literal
        | '(' expr ')'
        ;

arrayAccess     : id '[' expr ']'
                ;

call    : id '(' exprList* ')'
        ;

id      : ID
        ;

exprList        : expr (',' expr)*
                ;

literal : CONST_INT | CONST_CHAR | CONST_STRING | CONST_FLOAT | TRUE | FALSE
        ;

type:	TYPE_INT | TYPE_FLOAT | TYPE_CHAR | TYPE_STRING | TYPE_BOOL | TYPE_VOID
	;

compoundType : type '[' CONST_INT ']'
        | type
        ;

/*
 * #       ###### #    # ###### #####  
 * #       #       #  #  #      #    # 
 * #       #####    ##   #####  #    # 
 * #       #        ##   #      #####  
 * #       #       #  #  #      #   #  
 * ####### ###### #    # ###### #    # 
 */

/* Keywords */

TYPE_INT        : 'int'
	;

TYPE_FLOAT      : 'float'
	;

TYPE_CHAR       : 'char'
	;

TYPE_STRING     : 'string'
	;

TYPE_BOOL       : 'boolean'
	;

TYPE_VOID       : 'void'
	;

IF	: 'if'
	;

ELSE	: 'else'
	;

WHILE	: 'while'
	;

PRINT	: 'print'
	;

PRINTLN	: 'println'
	;

RETURN	: 'return'
	;

TRUE	: 'true'
	;

FALSE	: 'false'
	;

/* Punctuation */

BRACE_OPEN      : '{'
        ;

BRACE_CLOSE     : '}'
        ;

BRACKET_OPEN    : '['
        ;

BRACKET_CLOSE   : ']'
        ;

PAREN_OPEN      : '('
        ;

PAREN_CLOSE     : ')'
        ;

COMMA	        : ','
	;

SEMICOLON       : ';'
	;

/* Identifiers */

ID      : ('a'..'z' | 'A'..'Z' | '_')('a'..'z' | 'A'..'Z' | '0'..'9' | '_')*
	;

/* Binary operators */

OP_EQUAL_TO     : '=='
	;

OP_LESS_THAN    : '<'
	;

OP_ADD          : '+'
        ;

OP_SUB          : '-'
        ;

OP_MULT         : '*'
        ;

OP_ASSIGN       : '='
        ;

/* Constant values */

CONST_INT       : '-'? ('0' | ('1'..'9'('0'..'9')*))
        ;

CONST_STRING    : '"'  STR_CHARS* '"'
        ;

CONST_CHAR      : '\'' STR_CHARS '\''
        ;

fragment STR_CHARS : ('A'..'Z' | 'a'..'z' | '0'..'9' | '!' | ',' | '.' | ':' | '_' | '{' | '}' | ' ' )
        ;

CONST_FLOAT     : ('0'..'9')+'.'('0'..'9')+('('('0'..'9')+')')?
        ;

/* Whitespace */

WS      : ( '\t' | ' ' | ('\r' | '\n') )+ { $channel = HIDDEN;}
        ;

/* Comments */

COMMENT : '//' ~('\r' | '\n')* ('\r' | '\n') { $channel = HIDDEN;}
        ;
