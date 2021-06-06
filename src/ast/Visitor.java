package ast;

public abstract class Visitor<T> {
    public abstract T visit(Block b);

    public abstract T visit(ExprArrAcc ex);
    public abstract T visit(ExprBinaryOp ex);
    public abstract T visit(ExprFuncCall ex);
    public abstract T visit(ExprIden ex);
    public abstract <R> T visit(ExprLiteral<R> lt);
    public abstract T visit(ExprParen id);

    public abstract T visit(ExprList exprList);

    public abstract T visit(Function f);

    public abstract T visit(Program p);

    public abstract T visit(Stat st);
    public abstract T visit(StatArrAssn st);
    public abstract T visit(StatAssn st);
    public abstract T visit(StatExpr st);
    public abstract T visit(StatIf st);
    public abstract T visit(StatPrint st);
    public abstract T visit(StatReturn st);
    public abstract T visit(StatWhile st);

    public abstract T visit(VarDecl vd);
}
