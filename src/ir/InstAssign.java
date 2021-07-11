package ir;

public class InstAssign implements Instruction, Operand {
    public TempVar left, right;

    public InstAssign(TempVar left, TempVar right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " " + left.varType.toIRString() + " := " + right.toString();
    }
}
