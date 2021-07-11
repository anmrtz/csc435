package ir;

public class InstArrInit implements Instruction {
    TempVar arr;
    int arrSize;
    
    public InstArrInit(TempVar arr, int arrSize) {
        this.arr = arr;
        this.arrSize = arrSize;
    }

    @Override
    public String toString() {
        return arr.toString() + " := NEWARRAY " + arr.varType.toIRString() + " " + Integer.toString(arrSize);
    }
}
