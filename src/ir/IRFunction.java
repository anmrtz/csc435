package ir;

import java.util.ArrayList;

import ir.TempVar.TempType;
import type.Type;

public class IRFunction {
    public final String name;
    public final Type returnType;

    public ArrayList<Type> paramTypes = new ArrayList<Type>();
    public ArrayList<TempVar> temps = new ArrayList<TempVar>();

    public ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    public TempAllocator tempAllocator = new TempAllocator();


    public IRFunction(String funcId, Type funcType) {
        this.name = funcId;
        this.returnType = funcType;
    }

    public String toString() {
        String s = "FUNC " + name + " (";
        for (Type t : paramTypes) {
            s += t.toIRString();
        }
        s += ")" + returnType.toIRString() + "\n{\n";

        for (TempVar t : temps) {
            s += "    TEMP " + t.id + ":" + t.varType.toIRString();
            if (t.tempType == TempType.PARAM) {
                s += " [P(\"" + t.label + "\")]";
            }
            else if (t.tempType == TempType.LOCAL) {
                s += " [L(\"" + t.label + "\")]";
            }
            s += ";\n";
        }
        
        for (Instruction inst : instructions) {
            if (!(inst instanceof InstLabel)) {
                s += "    ";
            }
            s += inst.toString() + ";\n";
        }

        s += "}";

        return s;
    }
}
