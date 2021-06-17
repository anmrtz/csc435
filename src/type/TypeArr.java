package type;

public class TypeArr extends Type {
    public final int arrSize;

    public TypeArr(AtomicType typeID, int arrSize) {
        super(typeID);
        this.arrSize = arrSize;
    }

    @Override
    public boolean equals(Type other) {
        return false;
    }

    public boolean equals(TypeArr other) {
        return (this.arrSize == other.arrSize) && 
            (super.atomicType == other.atomicType);
    }

    @Override
    public String toString() {
        return super.toString() + '[' + arrSize + ']';
    }
}
