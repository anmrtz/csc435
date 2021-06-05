package ast;

import java.util.LinkedList;

public class ExprList extends ASTNode {
    
    public LinkedList<Expr> exprs = new LinkedList<Expr>();

    public void addExpr(Expr expr) {
        exprs.add(expr);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
