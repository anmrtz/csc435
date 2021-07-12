package ir;

import type.Type;

public class InstLiteralAssn<R> implements Instruction {
    public final Class<R> literalType;
    public R value;

    public TempVar temp;

    public InstLiteralAssn(Class<R> literalType, TempVar temp, R value) {
        this.literalType = literalType;
        this.temp = temp;
        this.value = value;
    }

    @Override
    public String toString() {
        String s = temp.toString() + " := ";

        if (temp.varType.atomicType.equals(Type.AtomicType.TYPE_CHAR)) {
            s += "\'" + value + "\'";
        }
        else if (temp.varType.atomicType.equals(Type.AtomicType.TYPE_BOOL)) {
            s += value.toString().toUpperCase();
        }
        else {
            s += value;
        }

        return s;
    }
}
