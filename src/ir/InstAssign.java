package ir;

import codegen.JVisitor;

public class InstAssign extends Instruction {
    public TempVar left, right;

    public InstAssign(TempVar left, TempVar right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " := " + right.toString();
    }

    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
