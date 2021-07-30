package ir;

import codegen.JVisitor;

public class InstReturn extends Instruction {
    public TempVar retValue = null;

    public InstReturn(TempVar retValue) {
        this.retValue = retValue;
    }

    @Override
    public String toString() {
        String s = "RETURN";
        if (retValue != null) {
            s += " " + retValue.toString();
        }

        return s;
    }

    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
