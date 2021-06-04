package ast;

import java.util.ArrayList;

public class FunctionBody extends ASTNode {

    public ArrayList<VarDecl> funcVars = new ArrayList<VarDecl>();
    public ArrayList<Statement> funcStats = new ArrayList<Statement>();;

    public FunctionBody() {
    }

    public void addVarDecl(VarDecl vd) {
        funcVars.add(vd);
    }

    public void addStatement(Statement st) {
        funcStats.add(st);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
