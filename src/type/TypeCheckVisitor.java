package type;

import java.util.*;

import type.Type.AtomicType;
import ast.*;

import java.io.*;

public class TypeCheckVisitor extends Visitor<Type> {

    public static class CompilerError {
        public final String errMsg;
        public final ASTNode node;

        public CompilerError(String errMsg, ASTNode node) {
            this.errMsg = errMsg;
            this.node = node;
        }

        // Sort compiler errors based on line/offset
        public static class LocationComparator implements Comparator<CompilerError> {
            @Override
            public int compare(CompilerError arg0, CompilerError arg1) {
                final int line0 = arg0.node.line;
                final int off0 = arg0.node.offset;
                final int line1 = arg1.node.line;
                final int off1 = arg1.node.offset;

                if (line0 < line1) {
                    return -1;
                }
                else if (line0 > line1) {
                    return 1;
                }
                else {
                    if (off0 < off1) {
                        return -1;
                    }
                    else if (off0 > off1) {
                        return 1;
                    }
                }

                return 0;
            }
        }

        @Override
        public String toString() {
            return String.format("[%d:%d] %s",node.line,node.offset,errMsg);
        }
    }

    private ArrayList<CompilerError> compilerErrors = new ArrayList<CompilerError>();

    private void raiseError(String errMsg, ASTNode node) {
        compilerErrors.add(new CompilerError(errMsg, node));
        compilerErrors.sort(new CompilerError.LocationComparator());
    }

    public boolean compilerErrors() {
        return !compilerErrors.isEmpty();
    }

    public void printCompilerErrors(PrintStream os) {
        for (CompilerError err : compilerErrors) {
            os.println(err.toString());
        }
    }

    private Environment<String,Function> funcDecs = new Environment<String,Function>();
    private Environment<String,Type> varDecs = new Environment<String,Type>();

    @Override
    public Type visit(Program p) {       
        // Program contains at least one function
        if (p.progFuncs.isEmpty()) {
            raiseError("program must contain at least one function", p);
        }

        funcDecs.beginScope(); 
        boolean mainFound = false;
        for (Function f : p.progFuncs) {
            f.accept(this);

            // Check for duplicate function name
            if (funcDecs.inCurrentScope(f.funcId)) {
                raiseError("duplicate function name", f);
            }

            // Check for main function
            if (f.funcId.equals("main")) {
                mainFound = true;
                // Check for void main
                if (f.funcType.atomicType != AtomicType.TYPE_VOID) {
                    raiseError("main function must have type void", f);
                }
            }
            funcDecs.add(f.funcId, f);            
        }

        if (!mainFound) {
            raiseError("program must contain a main function", p);
        }

        for (Function f : p.progFuncs) {
            f.accept(this);
        }

        funcDecs.endScope();
        return null;
    }

    @Override
    public Type visit(Function f) {
        // Check for duplicate parameters, duplicate locals, and local variables hiding parameters
        varDecs.beginScope();

        for (VarDecl vd : f.funcParams) {
            vd.accept(this);
        }

        for (VarDecl vd : f.funcVars) {
            vd.accept(this);
        }

        for (Stat st : f.funcStats) {
            final Type statType = st.accept(this);

            // Check that return type matches function type
            if ((st instanceof StatReturn) && !(statType.atomicType != AtomicType.TYPE_VOID)) {
                if (!statType.equals(f.funcType)) {
                    raiseError(String.format("return type does not match function type (%s to %s)", 
                        statType, f.funcType), st);
                }
            }
        }

        varDecs.endScope();
        return null;
    }

    @Override
    public Type visit(VarDecl vd) {
        if (varDecs.inCurrentScope(vd.varName.name)) {
            raiseError(String.format("variable \"%s\" already defined", vd.varName.name), vd);
        }

        if (vd.varType.atomicType == AtomicType.TYPE_VOID) {
            raiseError("variables must not have type void", vd);
        }

        varDecs.add(vd.varName.name, vd.varType);

        return null;
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
        final Type varType = varDecs.lookup(st.varName.name);

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
        return null;
    }

    @Override
    public Type visit(StatIf st) {
        final Type exprType = st.condExpr.accept(this);

        if ((exprType instanceof TypeArr) ||
            (exprType.atomicType != AtomicType.TYPE_BOOL)) {
            raiseError("if-statement condition must be type boolean", st);
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
            return new Type(AtomicType.TYPE_VOID);
        }
        else {
            return st.expr.accept(this);
        }
    }

    @Override
    public Type visit(StatWhile st) {
        final Type exprType = st.condExpr.accept(this);

        if ((exprType instanceof TypeArr) ||
            (exprType.atomicType != AtomicType.TYPE_BOOL)) {
            raiseError("while-statement condition must be type boolean", st);
        }

        return null;
    }

    @Override
    public Type visit(Block b) {
        return null;
    }

    @Override
    public Type visit(ExprArrAcc ex) {
        final Type arrType = varDecs.lookup(ex.id.name);

        if (arrType == null) {
            raiseError(String.format("undefined variable %s",ex.id.name), ex);
        }
        if (!(arrType instanceof TypeArr)) {
            raiseError("array access of non-array type", ex);
        }
        if (ex.idxExpr.accept(this).atomicType != AtomicType.TYPE_INT) {
            raiseError("array index must be an integer", ex);
        }

        // TypeArr -> Type
        return new Type(arrType.atomicType);
    }

    @Override
    public Type visit(ExprBinaryOp ex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(ExprFuncCall ex) {
        final Function function = funcDecs.lookup(ex.funcId);

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
                raiseError(String.format("function call parameter type mismatch (%d: %s to %s)",
                    i, callParamType, funcParamType), ex);            
            }
        }

        return function.funcType;
    }

    @Override
    public Type visit(ExprIden ex) {
        final Type varType = varDecs.lookup(ex.name);

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
