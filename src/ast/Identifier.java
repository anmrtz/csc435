package ast;

public class Identifier extends ASTNode {

    public String name;

    public Identifier (String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
