package ir;

import codegen.JVisitor;

public class Instruction extends IRNode {
    @Override
    public void accept(JVisitor j) {
        j.visit(this);
    }
}
