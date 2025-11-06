package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;

import org.apache.commons.lang3.Strings;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.AbstractInstanceInvokeExpr;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.ref.JParameterRef;
import sootup.core.jimple.common.stmt.InvokableStmt;
import sootup.core.jimple.common.stmt.JIdentityStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;

import java.util.HashSet;
import java.util.Set;

public class MethodCallOnParameterContains implements MethodFEU<Boolean> {

    private String value;

    public MethodCallOnParameterContains(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean contains = false;
        if (target.isConcrete()) {
            Set<Value> paramVals = new HashSet<Value>();
            for (Stmt u : target.getBody().getStmts()) {
                // Collect the parameters
                if (u instanceof JIdentityStmt) {
                    JIdentityStmt id = (JIdentityStmt) u;
                    if (id.getRightOp() instanceof JParameterRef)
                        paramVals.add(id.getLeftOp());
                }
                // Check for invocations
                if (u instanceof InvokableStmt && ((InvokableStmt) u).getInvokeExpr().isPresent()) {
                    AbstractInvokeExpr invokeExpr = ((InvokableStmt) u).getInvokeExpr().get();
                    if (invokeExpr instanceof AbstractInstanceInvokeExpr && paramVals.contains(((AbstractInstanceInvokeExpr) invokeExpr).getBase())) {
                        if (Strings.CI.contains(invokeExpr.getMethodSignature().getName(), value)) {
                            contains = true;
                            break;
                        }
                    }
                }
            }
        }
        return new Feature<>(getName(value), contains);
    }
}
