package ir;

import java.util.ArrayList;
import java.util.HashMap;

import ast.*;
import ir.TempVar.TempType;
import type.Type;
import type.TypeArr;
import type.Type.AtomicType;
import type.Environment;

public class IRVisitor extends Visitor<TempVar> {
    private HashMap<String,Type> funcRetTypes = new HashMap<String,Type>();
    private Environment<String,TempVar,IRFunction> environment = new Environment<String,TempVar,IRFunction>();

    public IRProgram program;

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
        // TODO
        return null;
    }

    @Override
    public TempVar visit(StatAssn st) {
        TempVar left = environment.lookup(st.varName.name);
        TempVar right = st.expr.accept(this);

        InstAssign ir = new InstAssign(left, right);
        addInstruction(ir);

        return null;
    }

    @Override
    public TempVar visit(StatExpr st) {
        return st.expr.accept(this);
    }

    @Override
    public TempVar visit(StatIf st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempVar visit(StatPrint st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempVar visit(StatReturn st) {
        TempVar retValue = null;
        
        if (st.expr != null) {
            st.expr.accept(this);
            addTemporary(retValue);
        }
        
        InstReturn ir = new InstReturn(retValue);
        addInstruction(ir);

        return retValue;
    }

    @Override
    public TempVar visit(StatWhile st) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempVar visit(Block b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempVar visit(ExprArrAcc ex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempVar visit(ExprBinaryOp ex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempVar visit(ExprFuncCall ex) {
        // TODO Auto-generated method stub
        return null;
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

        InstLiteralAssn<R> ir = new InstLiteralAssn<R>(lt.literalType, t, lt.value);
        addInstruction(ir);

        return t;
    }

    @Override
    public TempVar visit(ExprList exprList) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
