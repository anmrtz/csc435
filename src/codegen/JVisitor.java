package codegen;

import ir.*;
import type.*;
import type.Type.AtomicType;
import ast.ExprBinaryOp.OpType;

public class JVisitor {
    private StringBuilder sb = new StringBuilder();

    private IRProgram program;
    private int labelCount = 0;

    private boolean indent = false;

    private void tab() {
        indent = true;
    }

    private void untab() {
        indent = false;
    }

    private void append(String s) {
        sb.append((indent ? "\t" : "") + s);
    }

    private void appendln(String s) {
        append(s + "\n");
    }

    public JVisitor(IRProgram program) {
        this.program = program;
        this.program.accept(this);
    }

    public String getJCode() {
        return sb.toString();
    }

    public void visit(IRProgram ir) {
        appendln(".source " + this.program.name);
        appendln(".class public " + this.program.name);
        appendln(".super java/lang/Object\n");

        for (IRFunction f : ir.functions) {
            f.accept(this);;
            append("\n");
        }

        appendln(";-- Boilerplate --;\n");
        
        appendln(".method public static main([Ljava/lang/String;)V");
        tab();
        appendln(".limit locals 1");
        appendln(".limit stack 4");
        appendln("invokestatic " + this.program.name + "/__main()V");
        appendln("return");
        untab();
        appendln(".end method\n");
        
        appendln(".method public <init>()V");
        tab();
        appendln("aload_0");
        appendln("invokevirtual java/lang/Object/<init>()V");
        appendln("return");
        untab();
        appendln(".end method\n");
    }

    public void visit(IRFunction ir) {
        append(".method public static ");
        append(((ir.name == "main") ? "__main" : ir.name) + "(");
        
        for (Type t : ir.paramTypes) {
            append(t.toJString());
        }
        appendln(")" + ir.returnType.toJString());

        tab();
        appendln(".limit locals " + ir.temps.size());
        appendln(".limit stack  16");

        for (Instruction inst : ir.instructions) {
            inst.accept(this);;
            append("\n");
        }

        untab();
        appendln(".end method");
    }

    public void visit(Instruction ir) {
    }

    public void visit(InstArrAcc ir) {
        appendln("aload " + ir.arr.id);
        appendln("iload " + ir.arrIdx.id);

        appendln("iaload");
        appendln("istore " + ir.dest.id);
    }

    public void visit(InstArrAssn ir) {
        appendln("aload " + ir.destArr.id);
        appendln("iload " + ir.destIdx.id);

        ir.value.accept(this);

        appendln("iastore");
    }

    public void visit(InstArrInit ir) {
        Type type = ir.arr.varType;

        appendln("ldc " + ir.arrSize);

        if (type.atomicType == AtomicType.TYPE_STRING) {
            appendln("anewarray java/lang/String");
        }
        else if (type.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("newarray float");
        }
        else if (type.atomicType == AtomicType.TYPE_INT) {
            appendln("newarray int");
        }
        else if (type.atomicType == AtomicType.TYPE_CHAR) {
            appendln("newarray char");
        }
        else if (type.atomicType == AtomicType.TYPE_BOOL) {
            appendln("newarray boolean");
        }
    }

    public void visit(InstAssign ir) {
        Type type = ir.right.varType;

        ir.right.accept(this);

        // Array or string
        if ((type instanceof TypeArr) || 
            (type.atomicType == AtomicType.TYPE_STRING)) {
            appendln("astore " + ir.left.id);
        }
        else if (type.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("fstore " + ir.left.id);
        }
        // int, char, bool
        else {
            appendln("istore " + ir.left.id);
        }
    }

    public void visit(InstBinaryOp ir) {
        Type type = ir.left.varType;
        
        ir.left.accept(this);
        ir.right.accept(this);

        Character typeChar;
        // Array or string
        if ((type instanceof TypeArr) || 
            (type.atomicType == AtomicType.TYPE_STRING)) {
            typeChar = 'a';
        }
        else if (type.atomicType == AtomicType.TYPE_FLOAT) {
            typeChar = 'f';
        }
        // int, char, bool
        else {
            typeChar = 'i';
        }

        if (ir.opType.equals(OpType.OP_ADD)) {
            appendln(typeChar + "add");
        }
        else if (ir.opType.equals(OpType.OP_SUB)) {
            appendln(typeChar + "sub");            
        }
        else if (ir.opType.equals(OpType.OP_MULT)) {
            appendln(typeChar + "mul");
        }
        else if (ir.opType.equals(OpType.OP_LESS_THAN)) {
            
        }
        else if (ir.opType.equals(OpType.OP_EQUAL_TO)) {
            
        }

        // TODO: Store result to assignee
    }

    public void visit(InstCall ir) {
        for (TempVar temp : ir.params) {
            temp.accept(this);
        }

        append("invokestatic " + this.program.name + "/(");
        for (TempVar temp : ir.params) {
            temp.varType.toJString();
        }
        appendln(")" + this.program.funcRetTypes.get(ir.funcName).toJString());

        if (ir.assignee != null) {
            Type type = ir.assignee.varType;

            // Array or string
            if ((type instanceof TypeArr) || 
                (type.atomicType == AtomicType.TYPE_STRING)) {
                appendln("astore " + ir.assignee.id);
            }
            else if (type.atomicType == AtomicType.TYPE_FLOAT) {
                appendln("fstore " + ir.assignee.id);
            }
            // int, char, bool
            else {
                appendln("istore " + ir.assignee.id);
            }
        }
    }

    public void visit(InstJump ir) {
        TempVar cond = ir.condVar;

        if (cond == null) {
            appendln("goto " + ir.label.toString());
        }
        else {
            cond.accept(this);;
            appendln("ifne " + ir.label.toString());
        }
    }

    public void visit(InstLabel ir) {
        appendln(ir.toString());
    }

    public <R> void visit(InstLiteralAssn<R> ir) {
        Type type = ir.temp.varType;

        append("ldc ");
        if (ir.literalType.equals(Boolean.class)) {
            Boolean b = (Boolean)ir.value;
            appendln(b ? "1" : "0");
        }
        else if (ir.literalType.equals(Character.class)) {
            appendln("" + Character.getNumericValue((Character)ir.value));
        }
        else {
            appendln(ir.value.toString());
        }

        if (type.atomicType == AtomicType.TYPE_STRING) {
            appendln("astore " + ir.temp.id);
        }
        else if (type.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("fstore " + ir.temp.id);
        }
        // int, char, bool
        else if ((type.atomicType == AtomicType.TYPE_INT) 
        || (type.atomicType == AtomicType.TYPE_CHAR)
        || (type.atomicType == AtomicType.TYPE_BOOL)) {
            appendln("istore " + ir.temp.id);
        }
        else {
            throw new RuntimeException("Invalid literal constant assignee");
        }
    }

    public void visit(InstPrint ir) {
		Type type = ir.temp.varType;
		
        appendln("getstatic java/lang/System/out Ljava/io/PrintStream;");

        ir.temp.accept(this);

        if (ir.newLine) {
			append("invokevirtual java/io/PrintStream/println(");
        }
        else {
			append("invokevirtual java/io/PrintStream/print(");
        }

        if (type.atomicType == AtomicType.TYPE_STRING) {
            append("Ljava/lang/String;");
        }
        else {
            append(type.toJString());
        }
        appendln(")V");
    }

    public void visit(InstReturn ir) {
        TempVar retValue = ir.retValue;

        if (retValue == null) {
            appendln("return");
            return;
        }

        retValue.accept(this);;
        // Array or string
        if ((retValue.varType instanceof TypeArr) || 
            (retValue.varType.atomicType == AtomicType.TYPE_STRING)) {
            appendln("areturn");
        }
        else if (retValue.varType.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("freturn");
        }
        // int, char, bool
        else {
            appendln("ireturn");
        }
    }

    public void visit(InstUnary ir) {
        TempVar operand = ir.temp;
        Type type = operand.varType;

        if (ir.opType == InstUnary.OpType.OP_INVERT) {
            if (type.atomicType == AtomicType.TYPE_BOOL) {
                operand.accept(this);;
                appendln("ldc 1");
                appendln("ixor");
                appendln("istore " + operand.id);
            }
            else {
                throw new RuntimeException("Unsupported inversion operand");
            }
        }
        else if (ir.opType == InstUnary.OpType.OP_NEGATE) {
            if ((type.atomicType == AtomicType.TYPE_INT) 
            || (type.atomicType == AtomicType.TYPE_CHAR)
            || (type.atomicType == AtomicType.TYPE_BOOL)) {
                operand.accept(this);;
                appendln("ineg");
                appendln("istore " + operand.id);
            }
            else if (type.atomicType == AtomicType.TYPE_FLOAT) {
                operand.accept(this);;
                appendln("fneg");
                appendln("fstore " + operand.id);
            }
            else {
                throw new RuntimeException("Unsupported negation operand");
            }
        }
    }

    public void visit(TempVar ir) {
        Type type = ir.varType;

        // Array or string
        if ((type instanceof TypeArr) || 
            (type.atomicType == AtomicType.TYPE_STRING)) {
            appendln("aload " + ir.id);
        }
        else if (type.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("fload " + ir.id);
        }
        else if (type.atomicType == AtomicType.TYPE_VOID) {
            throw new RuntimeException("VOID temporary type");
        }
        // int, char, bool
        else {
            appendln("iload " + ir.id);
        }
    }
}
