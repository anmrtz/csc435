package ast;

import type.Type.TypeID;

public class PrintVisitor extends Visitor<Void> {

    private int currIndent = 0;

    private void print(String s) {
        System.out.print(s);
    }

    private void tab() {
        for (int i = 0; i < currIndent; ++i) {
            System.out.print("    ");
        }
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
        tab();
        print("{\n");
        this.currIndent++;
        for (VarDecl vd : fb.funcVars) {
            tab(); vd.accept(this);
            print(";\n");
        }
        
        for (Stat st : fb.funcStats) {
            tab(); st.accept(this);
            print("\n");
        }
        this.currIndent--;
        tab();
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
        if (l.getType() == TypeID.TYPE_CHAR) {
            print("\'");
            print(l.value.toString());
            print("\'");
        }
        else {
            print(l.value.toString());
        }

        return null;
    }

    @Override
    public Void visit(Stat p) {
        print(";");
        
        return null;
    }

    @Override
    public Void visit(StatArrAssn st) {
        st.arrAcc.accept(this);
        print(" = ");
        st.expr.accept(this);
        print(";");

        return null;
    }

    @Override
    public Void visit(StatAssn st) {
        st.varName.accept(this);
        print(" = ");
        st.expr.accept(this);
        print(";");
        
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
            tab();
            print("else\n");
            st.elseBlock.accept(this);
            print("\n");
        }

        return null;
    }

    @Override
    public Void visit(StatPrint st) {
        print(st.newLine ? "println " : "print ");
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
        tab();
        print("{\n");
        this.currIndent++;
        for (Stat st : b.blockStats) {
            tab(); st.accept(this);
            print("\n");
        }
        this.currIndent--;
        tab();
        print("}");

        return null;
    }

    @Override
    public Void visit(ExprIden id) {
        print(id.name);
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
    public Void visit(ExprFuncCall ex) {
        ex.funcId.accept(this);
        print("(");
        ex.params.accept(this);
        print(")");

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

    @Override
    public Void visit(ExprBinaryOp ex) {
        ex.el.accept(this);
        print(ex.getOpString());
        ex.er.accept(this);

        return null;
    }  
}
