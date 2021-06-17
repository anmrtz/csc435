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
        : (f = function {p.addFunction(f);})* EOF
	;

function returns [Function f]
        : fd = functionDecl fb = functionBody
        {
                f = new Function(fd, fb);
                f.setLine(fd.getLine());
        } 
	;

functionDecl returns [FunctionDecl fd]
        : tp = compoundType vId = id '(' (params = formalParameters)? ')'
        {
                fd = new FunctionDecl(tp, vId.name, params);
                fd.setLine(vId.getLine());
        }
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
        {
                vd = new VarDecl(tp, vId);
                vd.setLine(vId.getLine());
        }
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
        {
                st = new StatIf(condEx, ifBlk, elseBlk);
                st.setLine($IF.getLine());
        }
        ;

statWhile returns [StatWhile st]     
        : WHILE '(' condEx = expr ')' blk = block
        {
                st = new StatWhile(condEx, blk);
                st.setLine($WHILE.getLine());
        }
        ;

statPrint returns [StatPrint st]      
        : PRINT e = expr ';'
        {
                st = new StatPrint(e,false);
                st.setLine($PRINT.getLine());
        }
        ;

statPrintln returns [StatPrint st]    
        : PRINTLN e = expr ';'
        {
                st = new StatPrint(e,true);
                st.setLine($PRINTLN.getLine());
        }
        ;

statReturn returns [StatReturn st]     
        : RETURN (e = expr)? ';'
        {
                st = new StatReturn(e);
                st.setLine($RETURN.getLine());
        }
        ;

statArrAssn returns [StatArrAssn st]    
        : ac = arrayAccess '=' e = expr ';'
        {
                st = new StatArrAssn(ac, e);
                st.setLine(ac.getLine());
        }
        ;

statAssn returns [StatAssn st]       
        : varName = id '=' e = expr ';'
        {
                st = new StatAssn(varName, e);
                st.setLine(varName.getLine());
        }
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
        (OP_EQUAL_TO er = exprLessThan 
        {
                temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_EQUAL_TO,temp,er);
                temp.setLine($OP_EQUAL_TO.getLine());
        })*
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
        (OP_LESS_THAN er = exprAddSub {
                temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_LESS_THAN,temp,er);
                temp.setLine($OP_LESS_THAN.getLine());
        })*
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
        ((OP_ADD er = exprMult 
        {
                temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_ADD,temp,er);
                temp.setLine($OP_ADD.getLine());
        }) 
        | (OP_SUB er = exprMult 
        {
                temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_SUB,temp,er);
                temp.setLine($OP_SUB.getLine());
        }))*
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
        (OP_MULT er = atom 
        {
                temp = new ExprBinaryOp(ExprBinaryOp.OpType.OP_MULT,temp,er);
                temp.setLine($OP_MULT.getLine());
        })*
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
        : arrID = id '[' e = expr ']'
        {
                ac = new ExprArrAcc(arrID,e);
                ac.setLine(arrID.getLine());
        }
        ;

call returns [ExprFuncCall fc]   
        : funcID = id '(' el = exprList ')'
        {
                fc = new ExprFuncCall(funcID.name, el);
                fc.setLine(funcID.getLine());
        }
        ;

id      returns [ExprIden id]
        : name = ID
        {
                id = new ExprIden(name.getText());
                id.setLine(name.getLine());
        }
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
                        l.setLine(val.getLine());
                }
        | val = CONST_CHAR 
                {
                        char c = val.getText().charAt(1);
                        l = new ExprLiteral<Character>(Character.class, c);
                        l.setLine(val.getLine());
                }
        | val = CONST_STRING 
                {
                        l = new ExprLiteral<String>(String.class, val.getText());
                        l.setLine(val.getLine());
                }
        | val = CONST_FLOAT 
                {
                        double d = Double.parseDouble(val.getText());
                        l = new ExprLiteral<Double>(Double.class, d);
                        l.setLine(val.getLine());
                }
        | val = TRUE 
                {
                        l = new ExprLiteral<Boolean>(Boolean.class, true);
                        l.setLine(val.getLine());
                }
        | val = FALSE
                {
                        l = new ExprLiteral<Boolean>(Boolean.class, false);
                        l.setLine(val.getLine());
                }
        ;

type returns [Type t]
        : TYPE_INT {t = new Type(Type.AtomicType.TYPE_INT);} 
        | TYPE_FLOAT {t = new Type(Type.AtomicType.TYPE_FLOAT);} 
        | TYPE_CHAR {t = new Type(Type.AtomicType.TYPE_CHAR);}
        | TYPE_STRING {t = new Type(Type.AtomicType.TYPE_STRING);}
        | TYPE_BOOL {t = new Type(Type.AtomicType.TYPE_BOOL);}
        | TYPE_VOID {t = new Type(Type.AtomicType.TYPE_VOID);}
	;

compoundType returns [Type t]
        : tp = type '[' sz = CONST_INT ']' {t = new TypeArr(tp.atomicType, Integer.parseInt(sz.getText()));}
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
