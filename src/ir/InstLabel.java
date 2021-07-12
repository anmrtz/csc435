package ir;

public class InstLabel implements Instruction {
    public final int labelId;

    public InstLabel(int labelId) {
        this.labelId = labelId;
    }

    @Override
    public String toString() {
        return "L" + labelId + ":";
    }
}
