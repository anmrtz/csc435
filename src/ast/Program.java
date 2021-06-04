package ast;

import java.util.LinkedList;

public class Program extends ASTNode {

    public LinkedList<Function> progFuncs = new LinkedList<Function>();

    public Program() {
    }

    public void addFunction(Function f) {
        progFuncs.add(f);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }    
}
