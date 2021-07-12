package ir;

public class InstJump implements Instruction {
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
}
