package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.ref.JParameterRef;
import sootup.core.jimple.common.stmt.InvokableStmt;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JIdentityStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;

import java.util.HashSet;
import java.util.Set;


public class MethodParamFlowsToMethod implements MethodFEU<Boolean> {

    private String value;

    public MethodParamFlowsToMethod(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        if (target.isConcrete()) {
            Set<Value> paramVals = new HashSet<Value>();
            for (Stmt u : target.getBody().getStmts()) {
                // Collect the parameters
                if (u instanceof JIdentityStmt) {
                    JIdentityStmt id = (JIdentityStmt) u;
                    if (id.getRightOp() instanceof JParameterRef)
                        paramVals.add(id.getLeftOp());
                }

                if (u instanceof JAssignStmt) {
                    Value leftOp = ((JAssignStmt) u).getLeftOp();
                    Value rightOp = ((JAssignStmt) u).getRightOp();
                    if (paramVals.contains(leftOp)) paramVals.remove(leftOp);
                    if (paramVals.contains(rightOp)) {
                        paramVals.add(leftOp);
                    }
                }

                // Check for invocations
                if (u instanceof InvokableStmt && ((InvokableStmt) u).getInvokeExpr().isPresent()) {
                    AbstractInvokeExpr invokeExpr = ((InvokableStmt) u).getInvokeExpr().get();
                    if (invokeExpr.getMethodSignature().getName().toLowerCase()
                            .contains(value.toLowerCase())) {
                        for (Value arg : invokeExpr.getArgs()) {
                            if (paramVals.contains(arg)) {
                                return new Feature<>(getName(value), true);
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
