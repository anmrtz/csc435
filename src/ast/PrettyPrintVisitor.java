package ast;

public class PrettyPrintVisitor extends Visitor<Void> {

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
        print("\n");

        return null;
    }

    @Override
    public Void visit(FunctionBody fb) {
        print("{\n");

        for (VarDecl vd : fb.funcVars) {
            tab(); vd.accept(this);
            print(";\n");
        }

        for (Statement st : fb.funcStats) {
            tab(); st.accept(this);
            print("\n");
        }

        print("}");

        return null;
    }

    @Override
    public Void visit(FunctionDecl fd) {
        print(fd.funcType + " " + fd.funcId + "(");

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
        print(vd.varType + " " + vd.varName);

        return null;
    }

    @Override
    public <R> Void visit(Literal<R> l) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit(Statement p) {
        // TODO Auto-generated method stub
        return null;
    }
  
}
