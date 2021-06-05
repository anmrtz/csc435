package ast;

public class ExprLessThan extends Expr {

    public Expr el, er;

    public ExprLessThan(Expr el, Expr er) {
        this.el = el;
        this.er = er;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
