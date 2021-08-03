package codegen;

import ir.*;
import type.*;
import type.Type.AtomicType;
import ast.ExprBinaryOp.OpType;

public class JVisitor {
    private StringBuilder sb = new StringBuilder();

    private IRProgram program;
    private int cmpLabelCount;

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

    private void append(String s, boolean indent) {
        sb.append((indent ? "\t" : "") + s);
    }

    private void appendln(String s) {
        append(s + "\n");
    }

    private void appendln(String s, boolean indent) {
        append(s + "\n", indent);
    }

    public JVisitor(IRProgram program) {
        this.program = program;
        this.program.accept(this);
    }

    public String getJCode() {
        return sb.toString();
    }

    public void visit(IRProgram ir) {
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
        appendln("invokenonvirtual java/lang/Object/<init>()V");
        appendln("return");
        untab();
        appendln(".end method");
    }

    public void visit(IRFunction ir) {
        append(".method public static ");
        append(((ir.name.equals("main")) ? "__main" : ir.name) + "(");
        
        for (Type t : ir.paramTypes) {
            append(t.toJString());
        }
        appendln(")" + ir.returnType.toJString());

        tab();
        appendln(".limit locals " + ir.temps.size());
        appendln(".limit stack  16");

        // Default init locals to null / 0
        for (TempVar temp : ir.temps) {
            if (temp.tempType != TempVar.TempType.PARAM) {
                Type type = temp.varType;
                // Array or string
                if ((type instanceof TypeArr) || 
                    (type.atomicType == AtomicType.TYPE_STRING)) {
                        appendln("aconst_null");
                        appendln("astore " + temp.id);
                    }
                else if (type.atomicType == AtomicType.TYPE_FLOAT) {
                    appendln("ldc 0.0");
                    appendln("fstore " + temp.id);
                }
                // int, char, bool
                else {
                    appendln("ldc 0");
                    appendln("istore " + temp.id);
                }
            }
        }

        cmpLabelCount = 0;
        for (Instruction inst : ir.instructions) {
            inst.accept(this);;
            append("\n");
        }

        if (ir.instructions.isEmpty() || 
            !(ir.instructions.get(ir.instructions.size() - 1) instanceof InstReturn)) {
            
            Type retType = ir.returnType;
   
            // Array or string
            if ((retType instanceof TypeArr) || 
                (retType.atomicType == AtomicType.TYPE_STRING)) {
                appendln("aconst_null");
                appendln("areturn");
            }
            else if (retType.atomicType == AtomicType.TYPE_FLOAT) {
                appendln("ldc 0.0");
                appendln("freturn");
            }
            else if (retType.atomicType == AtomicType.TYPE_VOID) {
                appendln("return");
                return;
            }
            // int, char, bool
            else {
                appendln("ldc 0");
                appendln("ireturn");
            }
        }

        untab();
        appendln(".end method");
    }

    public void visit(Instruction ir) {
    }

    public void visit(InstArrAcc ir) {
        appendln("aload " + ir.arr.id);
        appendln("iload " + ir.arrIdx.id);

        Type type = ir.dest.varType;

        // Array or string
        if ((type instanceof TypeArr) || 
            (type.atomicType == AtomicType.TYPE_STRING)) {
            appendln("aaload");
            appendln("astore " + ir.dest.id);
        }
        else if (type.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("faload");
            appendln("fstore " + ir.dest.id);
        }
        else if (type.atomicType == AtomicType.TYPE_INT) {
            appendln("iaload");
            appendln("istore " + ir.dest.id);
        }
        else if (type.atomicType == AtomicType.TYPE_CHAR) {
            appendln("caload");
            appendln("istore " + ir.dest.id);
        }
        else if (type.atomicType == AtomicType.TYPE_BOOL) {
            appendln("baload");
            appendln("istore " + ir.dest.id);
        }
    }

    public void visit(InstArrAssn ir) {
        appendln("aload " + ir.destArr.id);
        appendln("iload " + ir.destIdx.id);

        ir.value.accept(this);

        Type type = ir.destArr.varType;
        if (type.atomicType == AtomicType.TYPE_STRING) {
            appendln("aastore");
        }
        else if (type.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("fastore");
        }
        else if (type.atomicType == AtomicType.TYPE_INT) {
            appendln("iastore");
        }
        else if (type.atomicType == AtomicType.TYPE_CHAR) {
            appendln("castore");
        }
        else if (type.atomicType == AtomicType.TYPE_BOOL) {
            appendln("bastore");
        }
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

        appendln("astore " + ir.arr.id);
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
        else if (ir.opType.equals(OpType.OP_LESS_THAN)
            || ir.opType.equals(OpType.OP_EQUAL_TO)) {

            if (type.atomicType == AtomicType.TYPE_FLOAT) {
                appendln("fcmpg");
            }
            else {
                appendln("isub");
            }

            cmpLabelCount += 2;
            if (ir.opType.equals(OpType.OP_LESS_THAN)) {
                append("iflt L_");
            }
            else {
                append("ifeq L_");                
            }
            append((cmpLabelCount-1) + "\n", false);

            appendln("ldc 0");
            appendln("goto L_" + cmpLabelCount);
            appendln("L_" + (cmpLabelCount-1) + ":", false);
            appendln("ldc 1");
            appendln("L_" + cmpLabelCount + ":", false);
        }

        Type destType = ir.dest.varType;
        // Array or string
        if ((destType instanceof TypeArr) || 
            (destType.atomicType == AtomicType.TYPE_STRING)) {
            appendln("astore " + ir.dest.id);
        }
        else if (destType.atomicType == AtomicType.TYPE_FLOAT) {
            appendln("fstore " + ir.dest.id);
        }
        // int, char, bool
        else {
            appendln("istore " + ir.dest.id);
        }
    }

    public void visit(InstCall ir) {
        for (TempVar temp : ir.params) {
            temp.accept(this);
        }

        append("invokestatic " + this.program.name + "/" + ir.funcName + "(");
        for (TempVar temp : ir.params) {
            append(temp.varType.toJString(), false);
        }
        appendln(")" + this.program.funcRetTypes.get(ir.funcName).toJString(), false);

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
            appendln("goto L" + ir.label.labelId);
        }
        else {
            cond.accept(this);;
            appendln("ifne L" + ir.label.labelId);
        }
    }

    public void visit(InstLabel ir) {
        appendln(ir.toString(), false);
    }

    public <R> void visit(InstLiteralAssn<R> ir) {
        Type type = ir.temp.varType;

        append("ldc ");
        if (ir.literalType.equals(Boolean.class)) {
            Boolean b = (Boolean)ir.value;
            appendln(b ? "1" : "0", false);
        }
        else if (ir.literalType.equals(Character.class)) {
            Character c = (Character)ir.value;
            appendln("" + (int)(c.charValue()), false);
        }
        else {
            appendln(ir.value.toString(), false);
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
            append("Ljava/lang/String;", false);
        }
        else {
            append(type.toJString(), false);
        }
        appendln(")V", false);
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
