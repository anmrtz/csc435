package ir;

import type.Type;

public class InstPrint implements Instruction {
    public TempVar temp;
    public final boolean newLine;

    public InstPrint(TempVar temp, boolean newLine) {
        this.temp = temp;
        this.newLine = newLine;
    }

    @Override
    public String toString() {
        String s = "PRINT";
        if (newLine) {
            s += "LN";
        }
        if (temp.varType.atomicType.equals(Type.AtomicType.TYPE_STRING)) {
            s += "U";
        }
        else {
            s += " " + temp.varType.toIRString();
        }

        s += " " + temp.toString();

        return s;
    }
}
