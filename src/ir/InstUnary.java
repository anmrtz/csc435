package ir;

public class InstUnary implements Instruction {
    static public enum OpType {
        OP_NEGATE,
        OP_INVERT
    } 
    
    public TempVar temp;
    public OpType opType;

    public InstUnary(TempVar temp, OpType opType) {
        this.temp = temp;
        this.opType = opType;
    }

    private String getOpString() {
        if (opType == OpType.OP_NEGATE) {
            return "-";
        }
        else {
            return "!";
        }
    }

    @Override 
    public String toString() {
        return temp.toString() + " := " + temp.varType.toIRString() + getOpString() + " " + temp.toString();
    }
}
