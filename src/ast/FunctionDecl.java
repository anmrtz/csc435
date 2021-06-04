package ast;

import type.*;
import java.util.LinkedList;
import java.util.List;

public class FunctionDecl extends ASTNode {

    public Type funcType;
    public Identifier funcId;
    public List<VarDecl> funcParams = new LinkedList<VarDecl>();

    public FunctionDecl(Type tp, Identifier id, List<VarDecl> params) {
        funcType = tp;
        funcId = id;
        if (params != null) {
            funcParams.addAll(params);
        }
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
