package ir;

import codegen.JVisitor;

public class InstLabel extends Instruction {
    public final int labelId;

    public InstLabel(int labelId) {
        this.labelId = labelId;
    }

    @Override
    public String toString() {
        return "L" + labelId + ":";
    }

    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
