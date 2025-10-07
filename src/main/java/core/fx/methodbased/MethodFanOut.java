package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.*;
import soot.jimple.InvokeExpr;

import java.util.HashSet;
import java.util.Set;

public class MethodFanOut implements MethodFEU<Integer> {
    @Override
    public Feature<Integer> extract(SootMethod target) {
        int fanOut = 0;
        Set<SootMethod> calledMethods = new HashSet<>();

        if (target.isConcrete() && target.hasActiveBody()) {
            Body body = target.getActiveBody();

            for (Unit u : body.getUnits()) {
                for (ValueBox vb : u.getUseBoxes()) {
                    Value v = vb.getValue();
                    if (v instanceof InvokeExpr) {
                        InvokeExpr invokeExpr = (InvokeExpr) v;
                        SootMethod calledMethod = invokeExpr.getMethod();
                        // Optional: ignore recursion
                        if (!calledMethod.equals(target)) {
                            calledMethods.add(calledMethod);
                        }
                    }
                }
            }
        }

        fanOut = calledMethods.size();
        return new Feature<>(getName(), fanOut);
    }
}
