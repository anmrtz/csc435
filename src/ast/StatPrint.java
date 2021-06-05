package ast;

public class StatPrint extends Stat {

    public Expr expr;
    public final boolean newLine; // Append newline character

    public StatPrint(Expr expr, boolean newLine) {
        this.expr = expr;
        this.newLine = newLine;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
