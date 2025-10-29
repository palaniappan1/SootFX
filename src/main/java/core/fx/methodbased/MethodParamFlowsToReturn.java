package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.ref.JParameterRef;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JIdentityStmt;
import sootup.core.jimple.common.stmt.JReturnStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;

import java.util.HashSet;
import java.util.Set;

public class MethodParamFlowsToReturn implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        if (target.isConcrete()) {
            Set<Value> paramVals = new HashSet<>();
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
                if (u instanceof JReturnStmt) {
                    JReturnStmt stmt = (JReturnStmt) u;
                    return new Feature<>(getName(), paramVals.contains(stmt.getOp()));
                }
            }
        }
        return new Feature<>(getName(), false);
    }
}
