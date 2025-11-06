package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.JCastExpr;
import sootup.core.jimple.common.ref.JParameterRef;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JIdentityStmt;
import sootup.core.jimple.common.stmt.JReturnStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;
import sootup.core.types.Type;

import java.util.HashMap;

public class MethodParamFlowsToReturn implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        if (target.isConcrete()) {
            HashMap<Value, Type> paramVals = new HashMap<>();
            for (Stmt u : target.getBody().getStmts()) {
                // Collect the parameters
                if (u instanceof JIdentityStmt) {
                    JIdentityStmt id = (JIdentityStmt) u;
                    if (id.getRightOp() instanceof JParameterRef) {
                        paramVals.put(id.getLeftOp(), id.getRightOp().getType());
                    }
                }
                if (u instanceof JAssignStmt) {
                    Value leftOp = ((JAssignStmt) u).getLeftOp();
                    if(leftOp instanceof JCastExpr) {
                        leftOp = ((JCastExpr) leftOp).getOp();
                    }
                    Value rightOp = ((JAssignStmt) u).getRightOp();
                    Type rightType = rightOp.getType();
                    if(rightOp instanceof JCastExpr) {
                        rightOp = ((JCastExpr) rightOp).getOp();
                    }
                    if (paramVals.containsKey(leftOp)) paramVals.remove(leftOp);
                    if (paramVals.containsKey(rightOp)) {
                        if (rightType.equals(paramVals.get(rightOp))) {
                            paramVals.put(leftOp, paramVals.get(rightOp));
                        }
                    }
                }

                // Check for invocations
                if (u instanceof JReturnStmt stmt) {
                    if(paramVals.containsKey(stmt.getOp())){
                        return new Feature<>(getName(), true);
                    }
                }
            }
        }
        return new Feature<>(getName(), false);
    }
}
