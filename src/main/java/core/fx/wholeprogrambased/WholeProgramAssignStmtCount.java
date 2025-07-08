package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.Body;
import soot.SootMethod;
import soot.jimple.AssignStmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 Count of all the assign statements present in the all the reachable methods
 */

public class WholeProgramAssignStmtCount extends WholeProgramMethodBasedFEU<Long> {

    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream()
                .filter(SootMethod::hasActiveBody)
                .map(SootMethod::getActiveBody)
                .flatMap(body -> body.getUnits().stream())
                .filter(unit -> unit instanceof AssignStmt)
                .count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
