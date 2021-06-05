package ast;

public class StatExpr extends Stat {

    public Expr expr;

    public StatExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
