package ast;

public class StatAssn extends Stat {

    public ExprIden varName;
    public Expr expr;

    public StatAssn(ExprIden varName, Expr expr) {
        this.varName = varName;
        this.expr = expr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
