package type;

public class Type {

    static public enum AtomicType {
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_CHAR,
        TYPE_STRING,
        TYPE_BOOL,
        TYPE_VOID
    }

    public final AtomicType atomicType;
   
    public Type(AtomicType atomicType) {
        this.atomicType = atomicType;
    }

    public boolean equals(Type other) {
        return this.atomicType == other.atomicType;
    }

    @Override
    public String toString() {
        switch (atomicType) {
            case TYPE_INT:
                return "int";
            case TYPE_FLOAT:
                return "float";
            case TYPE_CHAR:
                return "char";
            case TYPE_STRING:
                return "string";
            case TYPE_BOOL:
                return "boolean";
            case TYPE_VOID:
                return "void";
            default:
                return null;
        }
    }
}
