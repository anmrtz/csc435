package ast;

import java.util.LinkedList;

public class Function extends ASTNode {

    public FunctionDecl fd;
    public FunctionBody fb;

    public LinkedList<VarDecl> funcVars = new LinkedList<VarDecl>();

    public Function(FunctionDecl fd, FunctionBody fb) {
        this.fd = fd;
        this.fb = fb;

        funcVars.addAll(fd.funcParams);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
