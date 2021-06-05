package ast;

public class ExprAdd extends Expr {

    public Expr el, er;

    public ExprAdd(Expr el, Expr er) {
        this.el = el;
        this.er = er;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
