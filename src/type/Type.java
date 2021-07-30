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

    public <R> Type(Class<R> type) {
        this.atomicType = getAtomicFromClass(type);
    }

    public boolean equals(Type other) {
        if (other instanceof TypeArr) {
            return false;
        }
        return this.atomicType == other.atomicType;
    }

    private <R> AtomicType getAtomicFromClass(Class<R> literalType) {
        if (literalType.equals(Integer.class))
            return AtomicType.TYPE_INT;
        else if (literalType.equals(Double.class))
            return AtomicType.TYPE_FLOAT;
        else if (literalType.equals(Character.class))
            return AtomicType.TYPE_CHAR;
        else if (literalType.equals(String.class))
            return AtomicType.TYPE_STRING;
        else if (literalType.equals(Boolean.class))
            return AtomicType.TYPE_BOOL;
        else if (literalType.equals(Void.class))
            return AtomicType.TYPE_VOID;
        else
            throw new RuntimeException("Invalid literal class");
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

    public String toIRString() {
        switch (atomicType) {
            case TYPE_INT:
                return "I";
            case TYPE_FLOAT:
                return "F";
            case TYPE_CHAR:
                return "C";
            case TYPE_STRING:
                return "U";
            case TYPE_BOOL:
                return "Z";
            case TYPE_VOID:
                return "V";
            default:
                return null;
        }
    }

    public String toJString() {
        switch (atomicType) {
            case TYPE_INT:
                return "I";
            case TYPE_FLOAT:
                return "F";
            case TYPE_CHAR:
                return "C";
            case TYPE_STRING:
                return "Ljava/lang/String;";
            case TYPE_BOOL:
                return "Z";
            case TYPE_VOID:
                return "V";
            default:
                return null;
        }
    }
}
