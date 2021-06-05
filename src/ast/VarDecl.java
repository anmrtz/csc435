package ast;

import type.*;

public class VarDecl extends ASTNode {

    public Type varType;
    public ExprIden varName;

    public VarDecl(Type tp, ExprIden id) {
        varType = tp;
        varName = id;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
