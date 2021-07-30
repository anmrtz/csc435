package ir;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.HashMap;

import codegen.JVisitor;
import type.*;

public class IRProgram extends IRNode {
    public ArrayList<IRFunction> functions = new ArrayList<IRFunction>();
    public HashMap<String,Type> funcRetTypes = new HashMap<String,Type>();

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

    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
