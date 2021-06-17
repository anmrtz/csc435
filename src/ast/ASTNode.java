package ast;

public abstract class ASTNode {
    private int line = 0;

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public abstract <T> T accept(Visitor<T> v);
}
