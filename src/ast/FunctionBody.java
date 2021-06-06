package ast;

import java.util.ArrayList;

public class FunctionBody {

    public ArrayList<VarDecl> funcVars = new ArrayList<VarDecl>();
    public ArrayList<Stat> funcStats = new ArrayList<Stat>();;

    public void addVarDecl(VarDecl vd) {
        funcVars.add(vd);
    }

    public void addStatement(Stat st) {
        funcStats.add(st);
    }
}
