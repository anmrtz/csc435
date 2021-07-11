package ir;

import type.Type;

public class TempVar implements Operand {

    public enum TempType {
        LOCAL,
        PARAM,
        INTERMED
    }
    
    public final int id;

    public final Type varType;

    public final TempType tempType;
    public final String label;

    public TempVar(int id, Type varType, TempType tempType, String label) {
        this.id = id;
        this.varType = varType;
        this.tempType = tempType;
        this.label = label;
    }

    public String toString() {
        return ((tempType == TempType.PARAM) ? "P" : "T") + id;
    }
}
