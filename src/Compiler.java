/*
 * Compiler.java
 *
 * A starting place for the unnamed language compiler for CSC 435/535
 *
 */

import org.antlr.runtime.*;
import java.io.*;

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
			parser.program();
		}
		catch (RecognitionException e )	{
			// A lexical or parsing error occured.
			// ANTLR will have already printed information on the
			// console due to code added to the grammar.  So there is
			// nothing to do here.
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
