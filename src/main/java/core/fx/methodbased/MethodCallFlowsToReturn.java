package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import java.util.HashSet;
import java.util.Set;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.stmt.InvokableStmt;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JReturnStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;

public class MethodCallFlowsToReturn implements MethodFEU<Boolean> {

  String value;

  public MethodCallFlowsToReturn(String value) {
    this.value = value;
  }

  @Override
  public Feature<Boolean> extract(SootMethod sm) {
    if (sm.isConcrete()) {
      Set<Value> paramVals = new HashSet<Value>();

      for (Stmt u : sm.getBody().getStmts()) {
        // Check for invocations
        if (u instanceof InvokableStmt && ((InvokableStmt) u).getInvokeExpr().isPresent()) {
          AbstractInvokeExpr invokeExpr = ((InvokableStmt) u).getInvokeExpr().get();
          Value leftOp = null;
          if (u instanceof JAssignStmt) leftOp = ((JAssignStmt) u).getLeftOp();
          if (leftOp != null) paramVals.add(leftOp);
          // TODO: Add arguments as well? Not sure.
          if (invokeExpr
              .getMethodSignature()
              .getName()
              .toLowerCase()
              .contains(value.toLowerCase())) {
            paramVals.addAll(invokeExpr.getArgs());
          }
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
          return new Feature<>(getName(value), paramVals.contains(stmt.getOp()));
        }
      }
    }
    return new Feature<>(getName(value), false);
  }
}
