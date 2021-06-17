package ast;

public class StatArrAssn extends Stat {

    public ExprArrAcc arrAcc;
    public Expr expr;

    public StatArrAssn(ExprArrAcc arrAcc, Expr expr) {
        this.arrAcc = arrAcc;
        this.expr = expr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
