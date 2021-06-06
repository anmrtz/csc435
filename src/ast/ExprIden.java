package ast;

public class ExprIden extends Expr {
 
    public String name;

    public ExprIden(String name) {
        this.name = name;
    }

    public boolean equals(ExprIden other) {
        return this.name.equals(other.name);
    }

    public boolean equals(String other) {
        return this.name.equals(other);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }        
}
