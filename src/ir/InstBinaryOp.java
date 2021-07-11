package ir;
import ast.ExprBinaryOp.OpType;

public class InstBinaryOp implements Instruction, Operand {
    public TempVar left, right;
    public OpType opType;


    public InstBinaryOp(TempVar left, TempVar right, OpType opType) {
        this.left = left;
        this.right = right;
        this.opType = opType;
    }

    @Override
    public String toString() {
        return left.toString() + " " + left.varType.toIRString() + " = " + right.toString();
    }
}
