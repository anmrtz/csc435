package type;

public class TypeArr extends Type {
    public int arrSize;

    public TypeArr(TypeID typeID, int arrSize) {
        super(typeID);
        this.arrSize = arrSize;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + arrSize + ']';
    }
}
