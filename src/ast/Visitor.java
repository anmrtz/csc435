package ast;

public abstract class Visitor<T> {
    public abstract T visit(Function f);
    public abstract T visit(FunctionBody fb);
    public abstract T visit(FunctionDecl fd);
    public abstract <R> T visit(Literal<R> l);
    public abstract T visit(Program p);
    public abstract T visit(Statement p);
    public abstract T visit(VarDecl vd);
}
