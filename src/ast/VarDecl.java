package ast;

import type.*;

public class VarDecl extends ASTNode {

    public Type tp;
    public Identifier varId;

    public VarDecl(Type tp, Identifier id) {
        this.tp = tp;
        varId = id;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
