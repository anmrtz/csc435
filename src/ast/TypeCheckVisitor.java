package ast;

import type.*;
import type.Type.TypeID;

import java.util.*;
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

    @Override
    public Type visit(Program p) {       
        // Program contains at least one function
        if (p.progFuncs.isEmpty()) {
            raiseError("program must contain at least one function", p);
        }

        boolean mainFound = false;
        Set<String> funcNames = new HashSet<String>(); 
        for (Function f : p.progFuncs) {
            f.accept(this);

            // Check for duplicate function name
            if (!funcNames.add(f.funcId)) {
                raiseError("duplicate function name", f);
            }

            // Check for main function
            if (f.funcId.equals("main")) {
                mainFound = true;
                // Check for void main
                if (f.funcType.getType() != TypeID.TYPE_VOID) {
                    raiseError("main function must have type void", f);
                }
            }            
        }

        if (!mainFound) {
            raiseError("program must contain a main function", p);
        }

        return null;
    }

    @Override
    public Type visit(Function f) {
        // Check for duplicate parameters, duplicate locals, and local variables hiding parameters
        Set<String> varNames = new HashSet<String>();

        for (VarDecl vd : f.funcParams) {
            if (!varNames.add(vd.varName.name)) {
                raiseError("duplicate parameter name", vd);
            }

            if (vd.varType.getType() == TypeID.TYPE_VOID) {
                raiseError("parameter must not have type void", vd);
            }
        }

        for (VarDecl vd : f.funcVars) {
            if (!varNames.add(vd.varName.name)) {
                raiseError("variable name already defined", vd);
            }

            if (vd.varType.getType() == TypeID.TYPE_VOID) {
                raiseError("variable must not have type void", vd);
            }
        }

        for (Stat st : f.funcStats) {
            
        }

        // Check for variables being invoked before they are declared

        return null;
    }

    @Override
    public Type visit(VarDecl vd) {
        return null;
    }

    @Override
    public Type visit(Stat st) {
        return null;
    }

    @Override
    public Type visit(StatArrAssn st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(StatAssn st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(StatExpr st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(StatIf st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(StatPrint st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(StatReturn st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(StatWhile st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(Block b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(ExprArrAcc ex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(ExprBinaryOp ex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(ExprFuncCall ex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(ExprIden ex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(ExprParen id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <R> Type visit(ExprLiteral<R> lt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type visit(ExprList exprList) {
        // TODO Auto-generated method stub
        return null;
    }

}
