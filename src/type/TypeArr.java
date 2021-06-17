package type;

public class TypeArr extends Type {
    public final int arrSize;

    public TypeArr(AtomicType typeID, int arrSize) {
        super(typeID);
        this.arrSize = arrSize;
    }

    @Override
    public boolean equals(Type other) {
        if (other instanceof TypeArr) {
            TypeArr otherArr = (TypeArr)other;
            return (this.arrSize == otherArr.arrSize) && 
            (this.atomicType == otherArr.atomicType);
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + arrSize + ']';
    }
}
