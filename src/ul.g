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

block returns [Block b]
@init
{
        b = new Block();
}
        : '{' (st = statement {b.addStatement(st);})* '}'
        ;

/* Statements */
statement returns [Stat st] 
        : statEmpty {st = new Stat();}
        | si = statIf {st = si;}
        | sw = statWhile {st = sw;}
        | sp = statPrint {st = sp;}
        | spl = statPrintln {st = spl;}
        | sr = statReturn {st = sr;}
        | (id '=')=> sa = statAssn {st = sa;}
        | (arrayAccess '=')=> saa = statArrAssn {st = saa;}
        | se = statExpr {st = se;}
        ;

statEmpty       : ';'
                ;

statIf returns [StatIf st]         
        : IF '(' condEx = expr ')' ifBlk = block (ELSE elseBlk = block)?
        {st = new StatIf(condEx, ifBlk, elseBlk);}
        ;

statWhile returns [StatWhile st]     
        : WHILE '(' condEx = expr ')' blk = block
        {st = new StatWhile(condEx, blk);}
        ;

statPrint returns [StatPrint st]      
        : PRINT e = expr ';'
        {st = new StatPrint(e,false);}
        ;

statPrintln returns [StatPrint st]    
        : PRINTLN e = expr ';'
        {st = new StatPrint(e,true);}
        ;

statReturn returns [StatReturn st]     
        : RETURN (e = expr)? ';'
        {st = new StatReturn(e);}
        ;

statArrAssn returns [StatArrAssn st]    
        : ac = arrayAccess '=' e = expr ';'
        {st = new StatArrAssn(ac, e);}
        ;

statAssn returns [StatAssn st]       
        : varName = id '=' e = expr ';'
        {st = new StatAssn(varName, e);}
        ;

statExpr returns [StatExpr st]       
        : e = expr ';'
        {st = new StatExpr(e);}
        ;

/* Expressions */

expr returns [Expr e]           
        : e1 = exprEqualTo {e = e1;}
	;

exprEqualTo returns [Expr e]    
@init
{
        Expr temp = null;
}
@after
{
        e = temp;
}
        : el = exprLessThan {temp = el;} 
        ('==' er = exprLessThan {temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_EQUAL_TO,temp,er);})*
        ;

exprLessThan returns [Expr e]
@init
{
        Expr temp = null;
}
@after
{
        e = temp;
}
        : el = exprAddSub {temp = el;} 
        ('<' er = exprAddSub {temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_LESS_THAN,temp,er);})*
        ;

exprAddSub returns [Expr e]
@init
{
        Expr temp = null;
}
@after
{
        e = temp;
}
        : el = exprMult {temp = el;} 
        (('+' er = exprMult {temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_ADD,temp,er);}) 
        | ('-' er = exprMult {temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_SUB,temp,er);}))*
        ;

exprMult returns [Expr e]
@init
{
        Expr temp = null;
}
@after
{
        e = temp;
}
        : el = atom {temp = el;} 
        ('*' er = atom {temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_MULT,temp,er);})*
        ;

atom returns [Expr e]   
        :
        (id '[')=> ac = arrayAccess {e = ac;}
        | (id '(')=> fc = call {e = fc;}
        | ei = id {e = ei;}
        | l = literal {e = l;}
        | '(' e1 = expr ')' {e = new ExprParen(e1);}
        ;

arrayAccess returns [ExprArrAcc ac]    
        : arrID = id '[' e = expr ']' {ac = new ExprArrAcc(arrID,e);}
        ;

call returns [ExprFuncCall fc]   
        : funcID = id '(' el = exprList ')' {fc = new ExprFuncCall(funcID, el);}
        ;

id      returns [ExprIden id]
        : name = ID
        {id = new ExprIden(name.getText());}
        ;

exprList returns [ExprList el]       
@init
{
        el = new ExprList();
}
        : (e = expr {el.addExpr(e);} (',' e2 = expr {el.addExpr(e2);})*)?
        ;

literal returns [ExprLiteral l]
        : val = CONST_INT 
                {
                        int i = Integer.parseInt(val.getText());
                        l = new ExprLiteral<Integer>(Integer.class, i);
                }
        | val = CONST_CHAR 
                {
                        char c = val.getText().charAt(1);
                        l = new ExprLiteral<Character>(Character.class, c);
                }
        | val = CONST_STRING 
                {
                        l = new ExprLiteral<String>(String.class, val.getText());
                }
        | val = CONST_FLOAT 
                {
                        double d = Double.parseDouble(val.getText());
                        l = new ExprLiteral<Double>(Double.class, d);
                }
        | val = TRUE 
                {
                        l = new ExprLiteral<Boolean>(Boolean.class, true);
                }
        | val = FALSE
                {
                        l = new ExprLiteral<Boolean>(Boolean.class, false);
                }
        ;

type returns [Type t]
        : TYPE_INT {t = new Type(Type.TypeID.TYPE_INT);} 
        | TYPE_FLOAT {t = new Type(Type.TypeID.TYPE_FLOAT);} 
        | TYPE_CHAR {t = new Type(Type.TypeID.TYPE_CHAR);}
        | TYPE_STRING {t = new Type(Type.TypeID.TYPE_STRING);}
        | TYPE_BOOL {t = new Type(Type.TypeID.TYPE_BOOL);}
        | TYPE_VOID {t = new Type(Type.TypeID.TYPE_VOID);}
	;

compoundType returns [Type t]
        : tp = type '[' sz = CONST_INT ']' {t = new TypeArr(tp.getType(), Integer.parseInt(sz.getText()));}
        | tp = type {t = tp;}
        ;


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

CONST_INT       : ('0'..'9')+
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

COMMENT : '//' ~('\r' | '\n')* ('\r' | '\n' | EOF) { $channel = HIDDEN;}
        ;
