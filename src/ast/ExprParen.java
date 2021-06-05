package ast;

public class ExprParen extends Expr {
    public Expr expr;

    public ExprParen(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
