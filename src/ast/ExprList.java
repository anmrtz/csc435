package ast;

import java.util.ArrayList;

public class ExprList extends ASTNode {
    
    public ArrayList<Expr> exprs = new ArrayList<Expr>();

    public void addExpr(Expr expr) {
        exprs.add(expr);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
