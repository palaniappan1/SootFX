package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.SwitchStmt;

public class MethodCyclomaticComplexity implements MethodFEU<Integer> {
    @Override
    public Feature<Integer> extract(SootMethod target) {
        int cyclomaticComplexity = 0;
        if(target.isConcrete() && target.hasActiveBody()) {
            for(Unit u : target.getActiveBody().getUnits()) {
                if(u instanceof IfStmt) {
                    cyclomaticComplexity += 1;
                }
                if(u instanceof SwitchStmt){
                    cyclomaticComplexity += ((SwitchStmt) u).getTargets().size() - 1;
                }
            }
            cyclomaticComplexity += target.getActiveBody().getTraps().size();
            // For the default path
            cyclomaticComplexity += 1;
        }
        return new Feature<>(getName(), cyclomaticComplexity);
    }
}
