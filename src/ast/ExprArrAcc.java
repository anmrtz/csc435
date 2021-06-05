package ast;

public class ExprArrAcc extends Expr {

    public ExprIden id;
    public Expr idxExpr;

    public ExprArrAcc(ExprIden id, Expr idxExpr) {
        this.id = id;
        this.idxExpr = idxExpr;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
