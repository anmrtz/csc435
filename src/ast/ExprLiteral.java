package ast;

import type.Type.AtomicType;

public class ExprLiteral<R> extends Expr {
    private final Class<R> literalType;
    public R value;

    public ExprLiteral(Class<R> literalType, R value) {
        this.literalType = literalType;
        this.value = value;
    }

    public AtomicType getAtomicType() {
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
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
