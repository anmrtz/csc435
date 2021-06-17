package ast;

import java.util.ArrayList;

public class Block extends ASTNode {

    public ArrayList<Stat> blockStats = new ArrayList<Stat>();

    public void addStatement(Stat s) {
        blockStats.add(s);
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
