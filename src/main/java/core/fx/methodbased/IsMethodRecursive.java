package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

public class IsMethodRecursive implements MethodFEU<Boolean> {
    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean isRecursive = false;
        if(target.isConcrete() && target.hasActiveBody()) {
            for (Unit u : target.getActiveBody().getUnits()) {
                if (u instanceof InvokeStmt) {
                    InvokeStmt invokeStmt = (InvokeStmt) u;
                    if(invokeStmt.getInvokeExpr().getMethod().getSignature().equals(target.getSignature())){
                        isRecursive = true;
                        break;
                    }
                }

                for(ValueBox vb: u.getUseBoxes()){
                    Value v = vb.getValue();
                    if(v instanceof InvokeExpr){
                        InvokeExpr invokeExpr = (InvokeExpr) v;
                        if (invokeExpr.getMethod().getSignature().equals(target.getSignature())) {
                            isRecursive = true;
                            break;
                        }
                    }
                }
            }
        }
        return new Feature<>(getName(), isRecursive);
    }
}
