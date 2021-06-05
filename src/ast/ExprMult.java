package ast;

public class ExprMult extends Expr {

    public Expr el, er;

    public ExprMult(Expr el, Expr er) {
        this.el = el;
        this.er = er;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
