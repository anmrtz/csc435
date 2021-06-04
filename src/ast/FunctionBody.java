package ast;

import java.util.LinkedList;

public class FunctionBody extends ASTNode {

    public LinkedList<VarDecl> funcVars = new LinkedList<VarDecl>();
    public LinkedList<Statement> funcStats = new LinkedList<Statement>();;

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
