package ast;

import type.*;
import java.util.ArrayList;

public class FunctionDecl {

    public Type funcType;
    public ExprIden funcId;
    public ArrayList<VarDecl> funcParams = new ArrayList<VarDecl>();

    public FunctionDecl(Type tp, ExprIden id, ArrayList<VarDecl> params) {
        funcType = tp;
        funcId = id;
        if (params != null) {
            funcParams.addAll(params);
        }
    }   
}
