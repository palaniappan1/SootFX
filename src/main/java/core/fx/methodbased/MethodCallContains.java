package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.apache.commons.lang3.StringUtils;

import sootup.core.jimple.common.expr.AbstractInstanceInvokeExpr;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.stmt.InvokableStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;

public class MethodCallContains implements MethodFEU<Boolean> {

    private String value;

    public MethodCallContains(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean contains = false;
        if (target.isConcrete()) {
            for (Stmt u : target.getBody().getStmts()) {
                if (u instanceof InvokableStmt && ((InvokableStmt) u).getInvokeExpr().isPresent()) {
                    AbstractInvokeExpr invokeExpr = ((InvokableStmt) u).getInvokeExpr().get();
                    if (invokeExpr instanceof AbstractInstanceInvokeExpr ) {
                        if (StringUtils.containsIgnoreCase(invokeExpr.getMethodSignature().getName(), value)) {
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
