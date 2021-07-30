package ir;

import codegen.JVisitor;

import type.Type;

public class InstArrInit extends Instruction {
    public TempVar arr;
    public int arrSize;
    
    public InstArrInit(TempVar arr, int arrSize) {
        this.arr = arr;
        this.arrSize = arrSize;
    }

    @Override
    public String toString() {
        Type aType = new Type(arr.varType.atomicType);
        return arr.toString() + " := NEWARRAY " + aType.toIRString() + " " + Integer.toString(arrSize);
    }

    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
