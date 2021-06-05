package ast;

public class ExprSub extends Expr {

    public Expr el, er;

    public ExprSub(Expr el, Expr er) {
        this.el = el;
        this.er = er;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }        
}
