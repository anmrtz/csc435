package ir;

public class InstAssign implements Instruction {
    public TempVar left, right;

    public InstAssign(TempVar left, TempVar right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " := " + right.toString();
    }
}
