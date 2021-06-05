package ast;

import java.util.ArrayList;

public class Function extends ASTNode {

    public FunctionDecl fd;
    public FunctionBody fb;

    public ArrayList<VarDecl> funcVars = new ArrayList<VarDecl>();

    public Function(FunctionDecl fd, FunctionBody fb) {
        this.fd = fd;
        this.fb = fb;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
