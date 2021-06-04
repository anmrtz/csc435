package ast;

import type.*;

public abstract class Visitor<T> {
    public abstract T visit(Function f);
    public abstract T visit(FunctionBody fb);
    public abstract T visit(FunctionDecl fd);
    public abstract T visit(Identifier id);
    public abstract <R> T visit(Literal<R> l);
    public abstract T visit(Program p);
    public abstract T visit(Type tp);
    public abstract T visit(VarDecl vd);
}
