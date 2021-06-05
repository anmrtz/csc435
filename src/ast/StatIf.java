package ast;

public class StatIf extends Stat {

    public Expr condExpr;
    public Block ifBlock;
    public Block elseBlock; // May be null

    public StatIf(Expr condExpr, Block ifBlock, Block elseBlock) {
        this.condExpr = condExpr;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
