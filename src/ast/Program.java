package ast;

import java.util.ArrayList;

public class Program extends ASTNode {

    public ArrayList<Function> progFuncs = new ArrayList<Function>();

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
