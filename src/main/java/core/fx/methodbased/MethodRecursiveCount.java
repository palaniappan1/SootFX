package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

public class MethodRecursiveCount implements MethodFEU<Integer> {
    @Override
    public Feature<Integer> extract(SootMethod target) {
        int recursiveCallCount = 0;
        if (target.isConcrete() && target.hasActiveBody()) {
            Body body = target.getActiveBody();
            for (Unit u : body.getUnits()) {
                // Look for invoke statements
                if (u instanceof InvokeStmt) {
                    InvokeExpr invoke = ((InvokeStmt) u).getInvokeExpr();
                    if (invoke.getMethod().equals(target)) {
                        recursiveCallCount++;
                    }
                } else {
                    // Invokes can also appear in assignment statements
                    for (ValueBox vb : u.getUseBoxes()) {
                        Value v = vb.getValue();
                        if (v instanceof InvokeExpr) {
                            if (((InvokeExpr) v).getMethod().equals(target)) {
                                recursiveCallCount++;
                            }
                        }
                    }
                }
            }
        }
        return new Feature<>(getName(), recursiveCallCount);
    }
}
