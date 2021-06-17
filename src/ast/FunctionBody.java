package ast;

import java.util.ArrayList;

public class FunctionBody extends ASTNode {

    public ArrayList<VarDecl> funcVars = new ArrayList<VarDecl>();
    public ArrayList<Stat> funcStats = new ArrayList<Stat>();;

    public void addVarDecl(VarDecl vd) {
        funcVars.add(vd);
    }

    public void addStatement(Stat st) {
        funcStats.add(st);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return null;
    }
}
