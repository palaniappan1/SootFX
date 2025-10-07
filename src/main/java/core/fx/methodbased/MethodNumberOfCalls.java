package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

public class MethodNumberOfCalls implements MethodFEU<Integer> {
    @Override
    public Feature<Integer> extract(SootMethod target) {
        int numberOfCalls = 0;
        if(target.isConcrete() && target.hasActiveBody()) {
            for(Unit u : target.getActiveBody().getUnits()) {
                if(u instanceof InvokeStmt) {
                    numberOfCalls++;
                }
                else if(u instanceof AssignStmt) {
                    Value rightOp = ((AssignStmt) u).getRightOp();
                    if(rightOp instanceof InvokeExpr) {
                        numberOfCalls++;
                    }
                }
            }
        }
        return new Feature<>(getName(), numberOfCalls);
    }
}
