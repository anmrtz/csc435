package type;

import ast.*;

public class SemanticException extends RuntimeException {
    private final String errMsg;
    private final ASTNode node;

    public SemanticException(String errMsg, ASTNode node) {
        this.errMsg = errMsg;
        this.node = node;
    }  

    @Override
    public String toString() {
        return String.format("Error : %d : %s",node.getLine(),errMsg);
    }
}
