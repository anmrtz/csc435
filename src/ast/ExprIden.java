package ast;

public class ExprIden extends Expr {
 
    public String name;

    public ExprIden(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }        
}
