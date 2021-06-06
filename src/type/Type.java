package type;

public class Type {

    static public enum TypeID {
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_CHAR,
        TYPE_STRING,
        TYPE_BOOL,
        TYPE_VOID
    }

    private TypeID typeID;
   
    public Type(TypeID typeID) {
        this.typeID = typeID;
    }

    public TypeID getType() {
        return typeID;
    }

    public boolean equals(Type other) {
        return this.typeID.equals(other.typeID);
    }

    public boolean equals(TypeID other) {
        return this.typeID.equals(other);
    }

    @Override
    public String toString() {
        switch (typeID) {
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
