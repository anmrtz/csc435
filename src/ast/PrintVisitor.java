package ast;

import jdk.jshell.spi.ExecutionControl.RunException;
import java.lang.RuntimeException;

public class PrintVisitor extends Visitor<Void> {

    private void print(String s) {
        System.out.print(s);
    }

    private void tab() {
        System.out.print("    ");
    }

    @Override
    public Void visit(Function f) {
        f.fd.accept(this);
        print("\n");
        f.fb.accept(this);

        return null;
    }

    @Override
    public Void visit(FunctionBody fb) {
        print("{\n");

        for (VarDecl vd : fb.funcVars) {
            tab(); vd.accept(this);
            print(";\n");
        }

        for (Stat st : fb.funcStats) {
            tab(); st.accept(this);
            print("\n");
        }

        print("}");

        return null;
    }

    @Override
    public Void visit(FunctionDecl fd) {
        print(fd.funcType.toString() + " "); 
        fd.funcId.accept(this);
        print(" (");

        for (int i = 0; i < fd.funcParams.size(); ++i) {
            fd.funcParams.get(i).accept(this);
            if (i < fd.funcParams.size() - 1) 
                print(", ");
        }
        print(")");

        return null;
    }

    @Override
    public Void visit(Program p) {
        for (Function f : p.progFuncs) {
            f.accept(this);
            print("\n\n");
        }

        return null;
    }

    @Override
    public Void visit(VarDecl vd) {
        print(vd.varType.toString());
        print(" "); 
        vd.varName.accept(this);

        return null;
    }

    @Override
    public <R> Void visit(ExprLiteral<R> l) {
        print(l.value.toString());

        return null;
    }

    @Override
    public Void visit(Stat p) {
        print(";");
        
        return null;
    }

    @Override
    public Void visit(StatArrAssn st) {
        

        return null;
    }

    @Override
    public Void visit(StatAssn st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit(StatExpr st) {
        st.expr.accept(this);
        print(";");

        return null;
    }

    @Override
    public Void visit(StatIf st) {
        print("if (");
        st.condExpr.accept(this);
        print(")\n");
        st.ifBlock.accept(this);
        print("\n");
        if (st.elseBlock != null) {
            st.elseBlock.accept(this);
            print("\n");
        }

        return null;
    }

    @Override
    public Void visit(StatPrint st) {
        print("print ");
        st.expr.accept(this);
        print(";");

        return null;
    }

    @Override
    public Void visit(StatReturn st) {
        print("return ");
        if (st.expr != null) {
            st.expr.accept(this);
        }
        print(";");

        return null;
    }

    @Override
    public Void visit(StatWhile st) {
        print("while (");
        st.condExpr.accept(this);
        print(")\n");
        st.whileBlock.accept(this);
        print("\n");

        return null;
    }

    @Override
    public Void visit(Block b) {
        print("{\n");

        for (Stat st : b.blockStats) {
            tab(); st.accept(this);
            print("\n");
        }
        print("}");

        return null;
    }

    @Override
    public Void visit(ExprIden id) {
        print(id.name);
        return null;
    }

    @Override
    public Void visit(ExprAdd ex) {
        ex.el.accept(this);
        print("+");
        ex.er.accept(this);

        return null;
    }

    @Override
    public Void visit(ExprSub ex) {
        ex.el.accept(this);
        print("-");
        ex.er.accept(this);

        return null;
    }

    @Override
    public Void visit(ExprArrAcc ex) {
        ex.id.accept(this);
        print("[");
        ex.idxExpr.accept(this);
        print("]");
        
        return null;
    }

    @Override
    public Void visit(ExprEqualTo ex) {
        ex.el.accept(this);
        print("==");
        ex.er.accept(this);

        return null;
    }

    @Override
    public Void visit(ExprLessThan ex) {
        ex.el.accept(this);
        print("<");
        ex.er.accept(this);

        return null;
    }

    @Override
    public Void visit(ExprFuncCall ex) {
        ex.funcId.accept(this);
        print("(");
        ex.params.accept(this);
        print(")");

        return null;
    }

    @Override
    public Void visit(ExprMult ex) {
        ex.el.accept(this);
        print("*");
        ex.er.accept(this);

        return null;
    }

    @Override
    public Void visit(ExprParen ex) {
        print("(");
        ex.expr.accept(this);
        print(")");

        return null;
    }

    @Override
    public Void visit(ExprList exprList) {
        for (int i = 0; i < exprList.exprs.size(); ++i) {
            exprList.exprs.get(i).accept(this);
            if (i < exprList.exprs.size() - 1) {
                print(",");
            }
        }

        return null;
    }  
}
