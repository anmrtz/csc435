package type;

import ast.*;

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

}
