package ir;

public class InstReturn implements Instruction {
    TempVar retValue = null;

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
}
