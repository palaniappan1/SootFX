package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.*;

import java.util.HashSet;
import java.util.Set;

public class IsMethodRealGetter implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        if (target.getName().startsWith("get") && target.isConcrete()) {
            Set<Value> fieldVals = new HashSet<Value>();
            for (Unit unit : target.retrieveActiveBody().getUnits()) {
                if (unit instanceof AssignStmt) {
                    AssignStmt assignStmt = (AssignStmt) unit;
                    if (assignStmt.getRightOp() instanceof InstanceFieldRef) {
                        fieldVals.add(assignStmt.getLeftOp());
                    }
                    if (fieldVals.contains(assignStmt.getRightOp())) {
                        fieldVals.add(assignStmt.getLeftOp());
                    }
                    if (assignStmt.getRightOp() instanceof InvokeExpr) {
                        InvokeExpr inv = (InvokeExpr) assignStmt.getRightOp();
                        if (inv.getMethod().getName().startsWith("get")) {
                            fieldVals.add(assignStmt.getLeftOp());
                        }
                    }
                }
                if (unit instanceof ReturnStmt) {
                    ReturnStmt returnStmt = (ReturnStmt) unit;
                    Value returned = returnStmt.getOp();

                    // Check if the returned value is loaded from a field
                    if (fieldVals.contains(returned)) {
                        return new Feature<>(getName(), true);
                    }

                    // Directly returning a field reference
                    if (returned instanceof InstanceFieldRef) {
                        return new Feature<>(getName(), true);
                    }
                }
            }
        }
        return new Feature<>(getName(), false);
    }
}
