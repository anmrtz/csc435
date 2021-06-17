package ast;

public class ExprBinaryOp extends Expr {

    static public enum OpType {
        OP_LESS_THAN,
        OP_EQUAL_TO,
        OP_ADD,
        OP_SUB,
        OP_MULT
    }

    public OpType opType;
   
    public Expr el, er;

    public ExprBinaryOp(OpType opType, Expr el, Expr er) {
        this.opType = opType;
        this.el = el;
        this.er = er;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
