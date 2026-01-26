package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WholeProgramHardCodedStringsCount extends WholeProgramMethodBasedFEU<Integer> {
    @Override
    protected Feature<Integer> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        List<SootMethod> sootMethods = new ArrayList<>();
        for (SootMethod method : methods) {
            if (method != null && method.hasActiveBody()) {
                for (Unit u : method.getActiveBody().getUnits()) {
                    if (u instanceof AssignStmt) {
                        if (((AssignStmt) u).getRightOp() instanceof StringConstant) {
                            sootMethods.add(method);
                        }
                    }
                }
            }
        }
        return new Feature<>(this.getClass().getSimpleName(), sootMethods.size());
    }
}
