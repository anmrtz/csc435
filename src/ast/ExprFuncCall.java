package ast;

public class ExprFuncCall extends Expr {
    public ExprIden funcId;
    public ExprList params;

    public ExprFuncCall(ExprIden funcId, ExprList params) {
        this.funcId = funcId;
        this.params = params;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
