package ir;

public class InstArrAcc implements Instruction {
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
}
