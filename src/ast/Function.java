package ast;

import java.util.ArrayList;

import type.*;

public class Function extends ASTNode {

    public Type funcType;
    public ExprIden funcId;

    public ArrayList<VarDecl> funcParams;
    public ArrayList<VarDecl> funcVars;

    public ArrayList<Stat> funcStats;

    public Function(FunctionDecl fd, FunctionBody fb) {
        this.funcType = fd.funcType;
        this.funcId = fd.funcId;
        
        this.funcParams = fd.funcParams;
        this.funcVars = fb.funcVars;

        this.funcStats = fb.funcStats;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
