package ast;

import type.*;

public class VarDecl extends ASTNode {

    public Type varType;
    public String varName;

    public VarDecl(Type tp, String id) {
        varType = tp;
        varName = id;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
