package ir;

import java.util.ArrayList;
import java.util.StringJoiner;

public class InstCall implements Instruction {
    public final String funcName;
    public ArrayList<TempVar> params = new ArrayList<TempVar>();
    public TempVar assignee; // null if call result is not assigned to anything

    public InstCall(String funcName, TempVar assignee) {
        this.funcName = funcName;
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        String s = "";

        if (assignee != null) {
            s += assignee.toString() + " := ";
        }

        s += "CALL " + funcName + "(";
        StringJoiner sj = new StringJoiner(" ");
        for (TempVar t : params) {
            sj.add(t.toString());
        }
        s += sj.toString() + ")";

        return s;
    }
}
