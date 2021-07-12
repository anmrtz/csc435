package ir;

import java.util.ArrayList;
import java.util.StringJoiner;

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

        StringJoiner sj = new StringJoiner("\n\n");
        for (IRFunction func : functions) {
            sj.add(func.toString());
        }
        s += sj.toString() + "\n";

        return s;
    }
}
