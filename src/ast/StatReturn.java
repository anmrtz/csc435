package ast;

public class StatReturn extends Stat {

    public Expr expr = null; // Will be null for void return

    public StatReturn(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
