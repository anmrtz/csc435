package ast;

public class ExprIden extends Expr {
 
    public String name;

    public ExprIden(String name) {
        this.name = name;
    }

    public boolean equals(ExprIden other) {
        return this.name == other.name;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }        
}
