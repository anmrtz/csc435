package ast;

public class Literal<R> extends ASTNode {
    public Class<R> literalType;
    public R value;

    public Literal(Class<R> literalType, R value) {
        this.literalType = literalType;
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
