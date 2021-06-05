package ast;

public class StatWhile extends Stat {

    public Expr condExpr;
    public Block whileBlock;

    public StatWhile(Expr condExpr, Block whileBlock) {
        this.condExpr = condExpr;
        this.whileBlock = whileBlock;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }   
}
