package ast;

public class ExprLiteral<R> extends Expr {
    public Class<R> literalType;
    public R value;

    public ExprLiteral(Class<R> literalType, R value) {
        this.literalType = literalType;
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
