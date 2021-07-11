package ir;

import type.Type;

public class InstArrInit implements Instruction {
    TempVar arr;
    int arrSize;
    
    public InstArrInit(TempVar arr, int arrSize) {
        this.arr = arr;
        this.arrSize = arrSize;
    }

    @Override
    public String toString() {
        Type aType = new Type(arr.varType.atomicType);
        return arr.toString() + " := NEWARRAY " + aType.toIRString() + " " + Integer.toString(arrSize);
    }
}
