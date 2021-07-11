package ir;

import java.util.ArrayList;
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
        s += ")" + returnType.toIRString() + "\n{";

        for (TempVar t : temps) {
            s += "\tTEMP " + t.id + ":" + t.varType.toIRString() + ";";
        }
        
        for (Instruction inst : instructions) {
            if (!(inst instanceof InstLabel)) {
                s += "\t";
            }
            s += inst.toString() + ";\n";
        }

        s += "}\n";

        return s;
    }
}
