package ir;

import java.util.ArrayList;

public class IRProgram {
    public ArrayList<IRFunction> functions = new ArrayList<IRFunction>();
    public final String name;

    public IRProgram(String name) {
        this.name = name;
    }

    public void addFunction(IRFunction func) {
        functions.add(func);
    }

    @Override
    public String toString() {
        String s = "PROG " + name + "\n";

        for (IRFunction func : functions) {
            s += func.toString();
            s += "\n";
        }

        return s;
    }
}
