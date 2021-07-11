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

    public String getOpString() {
        return getOpString(this.opType);
    }

    public static String getOpString(OpType opType) {
        switch (opType) {
            case OP_LESS_THAN:
                return "<";
            case OP_EQUAL_TO:
                return "==";
            case OP_ADD:
                return "+";
            case OP_SUB:
                return "-";
            case OP_MULT:
                return "*";
            default:
                return null;
        }
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
