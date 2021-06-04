package ast;

import type.*;
import java.util.ArrayList;

public class FunctionDecl extends ASTNode {

    public Type funcType;
    public String funcId;
    public ArrayList<VarDecl> funcParams = new ArrayList<VarDecl>();

    public FunctionDecl(Type tp, String id, ArrayList<VarDecl> params) {
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
