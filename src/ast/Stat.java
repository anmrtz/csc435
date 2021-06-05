package ast;

public class Stat extends ASTNode {

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
