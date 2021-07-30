package ir;

import codegen.JVisitor;

public abstract class IRNode {
    public abstract void accept(JVisitor j);
}
