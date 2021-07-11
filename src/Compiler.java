import org.antlr.runtime.*;
import java.io.*;

import ast.*;
import ir.IRProgram;
import ir.IRVisitor;
import type.*;

public class Compiler {
	public static void main (String[] args) throws Exception {
		ANTLRInputStream input;

		if (args.length == 0 ) {
			System.out.println("Usage: Compiler filename.ul");
			System.exit(1);
			return;
		}
		else {
			input = new ANTLRInputStream(new FileInputStream(args[0]));
		}

		ulLexer lexer = new ulLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ulParser parser = new ulParser(tokens);

		try {
			Program p = parser.program();
		
			PrintVisitor pv = new PrintVisitor();
			p.accept(pv);

			TypeCheckVisitor tv = new TypeCheckVisitor();
			p.accept(tv);

			IRVisitor ir = new IRVisitor(args[0].split("\\.(?=[^\\.]+$)")[0]); 
			p.accept(ir);
			IRProgram irp = ir.program;
			System.out.print(irp.toString());
		}
		catch (SemanticException e) {
			System.out.println(e);
			System.exit(2);
		}
		catch (RecognitionException e )	{
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}
}
