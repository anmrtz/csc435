grammar ul;

@header
{
import ast.*;
import type.*;
import java.util.ArrayList;
}

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


program returns [Program p] 
@init
{
	// executed before the method starts
	p = new Program();
}
        : (f = function {p.addFunction(f);})+ EOF
	;

function returns [Function f]
        : fd = functionDecl fb = functionBody
        {f = new Function(fd, fb);}
	;

functionDecl returns [FunctionDecl fd]
        : tp = compoundType vId = id '(' (params = formalParameters)? ')'
        {fd = new FunctionDecl(tp, vId, params);}
	;

formalParameters returns [ArrayList<VarDecl> params]
@init
{
	params = new ArrayList<VarDecl>();
}
        : fp = varDecl {params.add(fp);} (',' fp2 = varDecl {params.add(fp2);})*
        ;

functionBody returns [FunctionBody fb]
@init
{
	fb = new FunctionBody();
}
        : '{' (vd = varDecl {fb.addVarDecl(vd);} ';')* (st = statement {fb.addStatement(st);})* '}'
	;

varDecl returns [VarDecl vd]
        : tp = compoundType vId = id    
        {vd = new VarDecl(tp, vId);}    
        ;

block   : '{' statement* '}'
        ;

/* Statements */
statement returns [Statement s] 
        : 
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

id      returns [String id]
        : name = ID
        {id = new String(name.getText());}
        ;

exprList        : expr (',' expr)*
                ;

literal returns [Literal l]
        : val = CONST_INT 
                {
                        int i = Integer.parseInt(val.getText());
                        l = new Literal<Integer>(Integer.class, i);
                }
        | val = CONST_CHAR 
                {
                        char c = val.getText().charAt(0);
                        l = new Literal<Character>(Character.class, c);
                }
        | val = CONST_STRING 
                {
                        l = new Literal<String>(String.class, val.getText());
                }
        | val = CONST_FLOAT 
                {
                        float f = Float.parseFloat(val.getText());
                        l = new Literal<Float>(Float.class, f);
                }
        | val = TRUE 
                {
                        l = new Literal<Boolean>(Boolean.class, true);
                }
        | val = FALSE
                {
                        l = new Literal<Boolean>(Boolean.class, false);
                }
        ;

type returns [Type t]
        : TYPE_INT {t = new Type(Type.TypeID.INT);} 
        | TYPE_FLOAT {t = new Type(Type.TypeID.FLOAT);} 
        | TYPE_CHAR {t = new Type(Type.TypeID.STRING);}
        | TYPE_STRING {t = new Type(Type.TypeID.STRING);}
        | TYPE_BOOL {t = new Type(Type.TypeID.BOOL);}
        | TYPE_VOID {t = new Type(Type.TypeID.VOID);}
	;

compoundType returns [Type t]
        : tp = type '[' sz = CONST_INT ']' {t = new TypeArr(tp.typeID, Integer.parseInt(sz.getText()));}
        | tp = type {t = tp;}
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
