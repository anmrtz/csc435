package ir;

public class InstArrAssn implements Instruction {
    public TempVar destArr, destIdx, value;

    public InstArrAssn(TempVar destArr, TempVar destIdx, TempVar value) {
        this.destArr = destArr;
        this.destIdx = destIdx;
        this.value = value;
    }

    @Override
    public String toString() {
        return destArr.toString() + "[" + destIdx.toString() + "] := " + value.toString();
    }
}
