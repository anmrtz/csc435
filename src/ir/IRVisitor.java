package ir;

import java.util.ArrayList;
import java.util.HashMap;

import ast.*;
import ir.InstUnary.OpType;
import ir.TempVar.TempType;
import type.Type;
import type.TypeArr;
import type.Type.AtomicType;
import type.Environment;

public class IRVisitor extends Visitor<TempVar> {
    private HashMap<String,Type> funcRetTypes = new HashMap<String,Type>();
    private Environment<String,TempVar,IRFunction> environment = new Environment<String,TempVar,IRFunction>();

    private int labelCount = 0;

    public IRProgram program;

    // Allocate a new GOTO label
    private InstLabel allocateLabel() {
        return new InstLabel(labelCount++);
    }

    // Add new instruction to current function
    private void addInstruction(Instruction inst) {
        environment.getScopeFunction().instructions.add(inst);
    }

    // Add new temporary to current function
    private void addTemporary(TempVar temp) {
        environment.getScopeFunction().temps.add(temp);
    }

    // Get temp allocator of current function
    private TempAllocator getTempAllocator() {
        return environment.getScopeFunction().tempAllocator;
    }

    public IRVisitor(String progName) {
        program = new IRProgram(progName);
    }

    @Override
    public TempVar visit(Program p) {
        for (Function f : p.progFuncs) {
            funcRetTypes.put(f.funcId, f.funcType);
        }

        for (Function f : p.progFuncs) {
            f.accept(this);
        }

        return null;
    }

    @Override
    public TempVar visit(Function f) {        
        IRFunction irFunc = new IRFunction(f.funcId, f.funcType);
        environment.beginScope(irFunc);

        for (VarDecl vd : f.funcParams) {
            TempVar t = getTempAllocator().allocate(vd.varType, TempType.PARAM, vd.varName.name);
            environment.add(vd.varName.name, t);
            addTemporary(t);
            irFunc.paramTypes.add(t.varType);
        }

        for (VarDecl vd : f.funcVars) {
            TempVar t = getTempAllocator().allocate(vd.varType, TempType.LOCAL, vd.varName.name);
            environment.add(vd.varName.name, t);
            addTemporary(t);

            if (vd.varType instanceof TypeArr) {
                TypeArr ta = (TypeArr)vd.varType;
                InstArrInit irArrInit = new InstArrInit(t,ta.arrSize);
                addInstruction(irArrInit);
            }
        }

        for (Stat st : f.funcStats) {
            st.accept(this);
        }

        if (f.funcType.atomicType.equals(Type.AtomicType.TYPE_VOID)) {
            addInstruction(new InstReturn(null));
        }

        environment.endScope();

        program.addFunction(irFunc);

        return null;
    }

    @Override
    public TempVar visit(VarDecl vd) {
        return null;
    }

    @Override
    public TempVar visit(Stat st) {
        return null;
    }

    @Override
    public TempVar visit(StatArrAssn st) {
        TempVar destArr = environment.lookup(st.arrAcc.id.name);
        TempVar destIdx = st.arrAcc.idxExpr.accept(this);

        TempVar value = st.expr.accept(this);

        InstArrAssn ir = new InstArrAssn(destArr, destIdx, value);
        addInstruction(ir);

        return null;
    }

    @Override
    public TempVar visit(StatAssn st) {
        TempVar left = environment.lookup(st.varName.name);

        TempVar right = null;
        Instruction ir = null;
        // Optimize out intermediate temporary if assigning constant directly to temp
        if (st.expr instanceof ExprLiteral<?>) {
            ExprLiteral<?> lt = (ExprLiteral<?>)st.expr;

            if (lt.literalType.equals(Integer.class))
                ir = new InstLiteralAssn<Integer>(Integer.class, left, (Integer)lt.value);
            else if (lt.literalType.equals(Double.class))
                ir = new InstLiteralAssn<Double>(Double.class, left, (Double)lt.value);
            else if (lt.literalType.equals(Character.class))
                ir = new InstLiteralAssn<Character>(Character.class, left, (Character)lt.value);
            else if (lt.literalType.equals(String.class))
                ir = new InstLiteralAssn<String>(String.class, left, (String)lt.value);
            else if (lt.literalType.equals(Boolean.class))
                ir = new InstLiteralAssn<Boolean>(Boolean.class, left, (Boolean)lt.value);
            else
                throw new RuntimeException("Invalid literal class");            
        }
        else {
            right = st.expr.accept(this);
            ir = new InstAssign(left, right);
        }

        addInstruction(ir);

        return null;
    }

    @Override
    public TempVar visit(StatExpr st) {
        return st.expr.accept(this);
    }

    @Override
    public TempVar visit(StatIf st) {
        TempVar condCheck = st.condExpr.accept(this);

        InstLabel labelEndIf = allocateLabel(); 

        addInstruction(new InstUnary(condCheck, OpType.OP_INVERT));
        addInstruction(new InstJump(labelEndIf, condCheck));

        st.ifBlock.accept(this);

        if (st.elseBlock != null) {
            InstLabel labelEndElse = allocateLabel();
            addInstruction(new InstJump(labelEndElse, null)); 
            addInstruction(labelEndIf);

            st.elseBlock.accept(this);
            
            addInstruction(labelEndElse);
        }
        else {
            addInstruction(labelEndIf);
        }

        return null;
    }

    @Override
    public TempVar visit(StatPrint st) {
        TempVar temp = st.expr.accept(this);

        InstPrint ir = new InstPrint(temp, st.newLine);
        addInstruction(ir);

        return null;
    }

    @Override
    public TempVar visit(StatReturn st) {
        TempVar retValue = null;
        
        if (st.expr != null) {
            retValue = st.expr.accept(this);
        }
        
        InstReturn ir = new InstReturn(retValue);
        addInstruction(ir);

        return retValue;
    }

    @Override
    public TempVar visit(StatWhile st) {
        InstLabel labelCondCheck = allocateLabel(); 
        InstLabel labelWhileEnd = allocateLabel();

        addInstruction(labelCondCheck);
        TempVar condCheck = st.condExpr.accept(this);

        addInstruction(new InstUnary(condCheck, OpType.OP_INVERT));
        addInstruction(new InstJump(labelWhileEnd, condCheck));

        st.whileBlock.accept(this);
        addInstruction(new InstJump(labelCondCheck, null));

        addInstruction(labelWhileEnd);        

        return null;
    }

    @Override
    public TempVar visit(Block b) {
        for (Stat s : b.blockStats) {
            s.accept(this);
        }

        return null;
    }

    @Override
    public TempVar visit(ExprArrAcc ex) {
        TempVar arrTemp = environment.lookup(ex.id.name);
        TempVar dest = getTempAllocator().allocate(new Type(arrTemp.varType.atomicType), TempType.INTERMED, null);
        addTemporary(dest);

        TempVar idxTemp = ex.idxExpr.accept(this);

        InstArrAcc ir = new InstArrAcc(dest,arrTemp,idxTemp);
        addInstruction(ir);

        return dest;
    }

    @Override
    public TempVar visit(ExprBinaryOp ex) {
        TempVar left = ex.el.accept(this);
        TempVar right = ex.er.accept(this);

        Type destType = null;
        if (ex.opType.equals(ExprBinaryOp.OpType.OP_EQUAL_TO) 
            || ex.opType.equals(ExprBinaryOp.OpType.OP_LESS_THAN)) {
            destType = new Type(Type.AtomicType.TYPE_BOOL);
        }
        else {
            destType = left.varType;
        }

        TempVar dest = getTempAllocator().allocate(destType, TempType.INTERMED, null);
        addTemporary(dest);

        InstBinaryOp ir = new InstBinaryOp(dest, left, right, ex.opType);
        addInstruction(ir);
        
        return dest;
    }

    @Override
    public TempVar visit(ExprFuncCall ex) {
        Type retType = funcRetTypes.get(ex.funcId);
        InstCall ir = null;
        TempVar assignee = null;
        if (!retType.equals(new Type(Type.AtomicType.TYPE_VOID))) {
            assignee = getTempAllocator().allocate(retType, TempType.INTERMED, null);
            addTemporary(assignee);
            ir = new InstCall(ex.funcId, assignee);
        }
        else {
            ir = new InstCall(ex.funcId, null);
        }

        for (Expr e : ex.params.exprs) {
            TempVar param = e.accept(this);
            if (param == null) {
                throw new RuntimeException("null param processed from ExprList");
            }
            ir.params.add(param);
        }

        addInstruction(ir);
        return assignee;
    }

    @Override
    public TempVar visit(ExprIden ex) {
        TempVar t = environment.lookup(ex.name);
        if (t == null) {
            throw new RuntimeException("Variable temp lookup failed");
        }
        return t;
    }

    @Override
    public TempVar visit(ExprParen id) {
        return id.expr.accept(this);
    }

    @Override
    public <R> TempVar visit(ExprLiteral<R> lt) {
        TempVar t = getTempAllocator().allocate(new Type(lt.literalType), TempType.INTERMED, null);
        addTemporary(t);

        InstLiteralAssn<R> ir = new InstLiteralAssn<R>(lt.literalType, t, lt.value);
        addInstruction(ir);

        return t;
    }

    @Override
    public TempVar visit(ExprList exprList) {
        return null;
    }
    
}
