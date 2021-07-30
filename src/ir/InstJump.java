package ir;

import codegen.JVisitor;

public class InstJump extends Instruction {
    public TempVar condVar = null; // If null, then jump is uncoditional GOTO
    public InstLabel label;

    public InstJump(InstLabel label, TempVar condVar) {
        this.label = label;
        this.condVar = condVar;
    }

    @Override
    public String toString() {
        String s = "";

        if (condVar != null) {
            s += "IF " + condVar.toString() + " ";
        }
        s += "GOTO " + "L" + label.labelId;
        
        return s;
    }

    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
