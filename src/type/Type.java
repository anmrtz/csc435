package type;

public class Type {

    static public enum TypeID {
        INT,
        FLOAT,
        CHAR,
        STRING,
        BOOL,
        VOID
    }

    public TypeID typeID;
   
    public Type(TypeID typeID) {
        this.typeID = typeID;
    }

    @Override
    public String toString() {
        switch (typeID) {
            case INT:
                return "int";
            case FLOAT:
                return "float";
            case CHAR:
                return "char";
            case STRING:
                return "string";
            case BOOL:
                return "boolean";
            case VOID:
                return "void";
            default:
                return null;
        }
    }
}
