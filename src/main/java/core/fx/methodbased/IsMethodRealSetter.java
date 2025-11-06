package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.expr.JCastExpr;
import sootup.core.jimple.common.ref.JInstanceFieldRef;
import sootup.core.jimple.common.ref.JParameterRef;
import sootup.core.jimple.common.stmt.InvokableStmt;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JIdentityStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;
import java.util.HashSet;
import java.util.Set;

public class IsMethodRealSetter implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        if (target.getName().startsWith("set") && target.isConcrete()){
            Set<Value> paramVals = new HashSet<Value>();
            for (Stmt u : target.getBody().getStmts()) {
                if (u instanceof JIdentityStmt) {
                    JIdentityStmt id = (JIdentityStmt) u;
                    if (id.getRightOp() instanceof JParameterRef){
                        paramVals.add(id.getLeftOp());
                    }
                } else if (u instanceof JAssignStmt) {
                    JAssignStmt assign = (JAssignStmt) u;
                    Value rightOp = ((JAssignStmt) u).getRightOp();
                    if(rightOp instanceof JCastExpr) {
                        rightOp = ((JCastExpr) rightOp).getOp();
                    }
                    if (paramVals.contains(rightOp)){
                        if (assign.getLeftOp() instanceof JInstanceFieldRef){
                            return new Feature<>(getName(), true);
                        }
                    }
                }
                if (u instanceof InvokableStmt && ((InvokableStmt) u).getInvokeExpr().isPresent()) {
                    AbstractInvokeExpr invokeExpr = ((InvokableStmt) u).getInvokeExpr().get();
                    if (invokeExpr.getMethodSignature().getName().startsWith("get")) {
                        for (Value arg : invokeExpr.getArgs()) {
                            if (paramVals.contains(arg)) {
                                return new Feature<>(getName(), true);
                            }
                        }
                    }
                }

            }
            return new Feature<>(getName(), false);
        }
        return new Feature<>(getName(), false);
    }
}
