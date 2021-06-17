package type;

import java.util.*;

import ast.*;

public class SemanticException extends RuntimeException {
    public final String errMsg;
    public final ASTNode node;

    public SemanticException(String errMsg, ASTNode node) {
        this.errMsg = errMsg;
        this.node = node;
    }  

    // Sort compiler errors based on line/offset
    public static class LocationComparator implements Comparator<SemanticException> {
        @Override
        public int compare(SemanticException arg0, SemanticException arg1) {
            final int line0 = arg0.node.line;
            final int off0 = arg0.node.offset;
            final int line1 = arg1.node.line;
            final int off1 = arg1.node.offset;

            if (line0 < line1) {
                return -1;
            }
            else if (line0 > line1) {
                return 1;
            }
            else {
                if (off0 < off1) {
                    return -1;
                }
                else if (off0 > off1) {
                    return 1;
                }
            }

            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("[%d:%d] %s",node.line,node.offset,errMsg);
    }
}
