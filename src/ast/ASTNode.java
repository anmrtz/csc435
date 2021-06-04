package ast;

public abstract class ASTNode {
    public int line, offset;

    public abstract <T> T accept(Visitor<T> v);
}
