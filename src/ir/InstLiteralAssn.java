package ir;

public class InstLiteralAssn<R> implements Instruction, Operand {
    public final Class<R> literalType;
    public R value;

    public TempVar temp;

    public InstLiteralAssn(Class<R> literalType, TempVar temp, R value) {
        this.literalType = literalType;
        this.value = value;
    }

    @Override
    public String toString() {
        return temp.toString() + " " + temp.varType.toIRString() + " := " + value;
    }
}
