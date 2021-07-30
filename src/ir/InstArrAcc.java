package ir;

import codegen.JVisitor;

public class InstArrAcc extends Instruction {
    public TempVar dest, arr, arrIdx;

    public InstArrAcc(TempVar dest, TempVar arr, TempVar arrIdx) {
        this.dest = dest;
        this.arr = arr;
        this.arrIdx = arrIdx;
    }

    @Override
    public String toString() {
        return dest.toString() + " := " + arr.toString() + "[" + arrIdx.toString() + "]";
    }

    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
