package type;

import ast.*;
import ast.ExprBinaryOp.*;
import type.Type.AtomicType;

import java.util.HashMap;

public class TypeCheckVisitor extends Visitor<Type> {

    private HashMap<String,Function> funcDecs = new HashMap<String,Function>();
    private Environment<String,Type> environment = new Environment<String,Type>();

    private void raiseError(String errMsg, ASTNode node) throws SemanticException {
        throw new SemanticException(errMsg, node);
    }

    @Override
    public Type visit(Program p) {       
        // Program contains at least one function
        if (p.progFuncs.isEmpty()) {
            raiseError("program must contain at least one function", p);
        }

        boolean mainFound = false;
        for (Function f : p.progFuncs) {
            // Check for duplicate function name
            if (funcDecs.containsKey(f.funcId)) {
                raiseError("duplicate function name", f);
            }

            // Check for main function
            if (f.funcId.equals("main")) {
                mainFound = true;
                // Check for void main
                if (f.funcType.atomicType != AtomicType.TYPE_VOID) {
                    raiseError("main function must have type void", f);
                }
                // Check for empty parameter list
                if (!f.funcParams.isEmpty()) {
                    raiseError("main function must have no parameters", f);
                }
            }
            funcDecs.put(f.funcId, f);            
        }

        if (!mainFound) {
            raiseError("program must contain a main function", p);
        }

        for (Function f : p.progFuncs) {
            f.accept(this);
        }

        return null;
    }

    @Override
    public Type visit(Function f) {
        // Check for duplicate parameters, duplicate locals, and local variables hiding parameters
        environment.beginScope(f);

        for (VarDecl vd : f.funcParams) {
            if (environment.inCurrentScope(vd.varName.name)) {
                raiseError(String.format("variable \"%s\" already defined", vd.varName.name), vd);
            }
    
            if (vd.varType.atomicType == AtomicType.TYPE_VOID) {
                raiseError("variables must not have type void", vd);
            }
 
            environment.add(vd.varName.name, vd.varType);
        }

        for (VarDecl vd : f.funcVars) {
            if (environment.inCurrentScope(vd.varName.name)) {
                raiseError(String.format("variable \"%s\" already defined", vd.varName.name), vd);
            }
    
            if (vd.varType.atomicType == AtomicType.TYPE_VOID) {
                raiseError("variables must not have type void", vd);
            }

            environment.add(vd.varName.name, vd.varType);
        }

        for (Stat st : f.funcStats) {
            st.accept(this);
        }

        environment.endScope();
        return null;
    }

    @Override
    public Type visit(VarDecl vd) {
        return vd.varType;
    }

    @Override
    public Type visit(Stat st) {
        return null;
    }

    @Override
    public Type visit(StatArrAssn st) {
        final Type arrAtomicType = st.arrAcc.accept(this);
        final Type exprType = st.expr.accept(this);

        if (!arrAtomicType.equals(exprType)) {
            raiseError(String.format("incompatible type for array assignment (%s to %s)", exprType, arrAtomicType), st);
        }

        return null;
    }

    @Override
    public Type visit(StatAssn st) {
        final Type varType = environment.lookup(st.varName.name);

        if (varType == null) {
            raiseError(String.format("undefined variable %s",st.varName.name), st);
        }

        final Type exprType = st.expr.accept(this);
        if (!varType.equals(exprType)) {
            raiseError(String.format("incompatible type for assignment (%s to %s)", exprType, varType), st);
        }

        return null;
    }

    @Override
    public Type visit(StatExpr st) {
        st.expr.accept(this);

        return null;
    }

    @Override
    public Type visit(StatIf st) {
        final Type exprType = st.condExpr.accept(this);

        if ((exprType instanceof TypeArr) ||
            (exprType.atomicType != AtomicType.TYPE_BOOL)) {
            raiseError("if-statement condition must be type boolean", st);
        }

        st.ifBlock.accept(this);
        if (st.elseBlock != null) {
            st.elseBlock.accept(this);
        }

        return null;
    }

    @Override
    public Type visit(StatPrint st) {
        final Type exprType = st.expr.accept(this);

        if ((exprType instanceof TypeArr) || 
            (exprType.atomicType == AtomicType.TYPE_VOID)) {
            raiseError("print statement expression must be non-void atomic", st);
        }

        return null;
    }

    @Override
    public Type visit(StatReturn st) {
        if (st.expr == null) {
            return null;
        }
        
        final Type returnType = st.expr.accept(this);

        // Check that return type matches function type
        if (returnType.atomicType != AtomicType.TYPE_VOID) {
            final Function scopeFunction = environment.getScopeFunction();

            if (!returnType.equals(scopeFunction.funcType)) {
                raiseError(String.format("return type does not match function type (%s to %s)", 
                    returnType, scopeFunction.funcType), st);
            }
        }

        return null;
    }

    @Override
    public Type visit(StatWhile st) {
        final Type exprType = st.condExpr.accept(this);

        if ((exprType instanceof TypeArr) ||
            (exprType.atomicType != AtomicType.TYPE_BOOL)) {
            raiseError("while-statement condition must be type boolean", st);
        }

        st.whileBlock.accept(this);

        return null;
    }

    @Override
    public Type visit(Block b) {
        for (Stat st : b.blockStats) {
            st.accept(this);
        }

        return null;
    }

    @Override
    public Type visit(ExprArrAcc ex) {
        final Type arrType = environment.lookup(ex.id.name);

        if (arrType == null) {
            raiseError(String.format("undefined variable %s",ex.id.name), ex);
        }
        if (!(arrType instanceof TypeArr)) {
            raiseError("array access of non-array type", ex);
        }
        if (!ex.idxExpr.accept(this).equals(new Type(AtomicType.TYPE_INT))) {
            raiseError("array index must be an integer", ex);
        }

        // TypeArr -> Type
        return new Type(arrType.atomicType);
    }

    private boolean validOperand(OpType op, Type type) {
        final AtomicType atomicType = type.atomicType;

        if (atomicType == AtomicType.TYPE_VOID) {
            return false;
        }

        switch (op) {
            case OP_ADD:
                return (atomicType != AtomicType.TYPE_BOOL);
            case OP_SUB:
                return (atomicType != AtomicType.TYPE_BOOL)
                    && (atomicType != AtomicType.TYPE_STRING);
            case OP_MULT:
                return (atomicType != AtomicType.TYPE_BOOL)
                    && (atomicType != AtomicType.TYPE_STRING)
                    && (atomicType != AtomicType.TYPE_CHAR);
            case OP_EQUAL_TO:
                return true;
            case OP_LESS_THAN:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Type visit(ExprBinaryOp ex) {
        final Type leftType = ex.el.accept(this);
        final Type rightType = ex.er.accept(this);

        if ((leftType instanceof TypeArr) || (rightType instanceof TypeArr)) {
            raiseError("binary operands must be non-void atomic type", ex);
        }

        if (!validOperand(ex.opType, leftType)) {
            raiseError(String.format("invalid left binary operand type (%s %s)", 
                leftType, ex.getOpString()), ex);
        }
        if (!validOperand(ex.opType, rightType)) {
            raiseError(String.format("invalid right binary operand type (%s %s)", 
                ex.getOpString(), rightType), ex);
        }
        if (!leftType.equals(rightType)) {
            raiseError(String.format("binary operand mismatch (%s %s %s)", 
                leftType, ex.getOpString(), rightType), ex);
        }

        if ((ex.opType == OpType.OP_EQUAL_TO) || (ex.opType == OpType.OP_LESS_THAN)) {
            return new Type(AtomicType.TYPE_BOOL);
        }
        else {
            return new Type(leftType.atomicType);
        }
    }

    @Override
    public Type visit(ExprFuncCall ex) {
        final Function function = funcDecs.get(ex.funcId);

        // Check that function exists
        if (function == null) {
            raiseError(String.format("undefined function %s", ex.funcId), ex);
        }

        // Check that call parameters match function parameters
        final int callParamsLen = ex.params.exprs.size();
        if (callParamsLen != function.funcParams.size()) {
            raiseError(String.format("function call parameter size mismatch (%d to %d)",
                callParamsLen,function.funcParams.size()), ex);            
        }
        for (int i = 0; i < callParamsLen; i++) {
            final Type callParamType = ex.params.exprs.get(i).accept(this);
            final Type funcParamType = function.funcParams.get(i).accept(this);

            if (!(callParamType.equals(funcParamType))) {
                raiseError(String.format("function call parameter type mismatch (%s to %s)",
                    callParamType, funcParamType), ex);            
            }
        }

        return function.funcType;
    }

    @Override
    public Type visit(ExprIden ex) {
        final Type varType = environment.lookup(ex.name);

        if (varType == null) {
            raiseError(String.format("undefined variable %s", ex.name), ex);
        }

        return varType;
    }

    @Override
    public Type visit(ExprParen id) {
        return id.expr.accept(this);
    }

    @Override
    public <R> Type visit(ExprLiteral<R> lt) {
        return new Type(lt.getAtomicType());
    }

    @Override
    public Type visit(ExprList exprList) {
        return null;
    }

}
