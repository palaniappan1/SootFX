package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.Body;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/*
Gives the number of statements present in the all the reachable methods
 */

public class WholeProgramStmtCount extends WholeProgramMethodBasedFEU<Long> {

    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream().filter(Objects::nonNull)
                .filter(SootMethod::hasActiveBody)
                .map(SootMethod::getActiveBody)
                .mapToLong(body -> body.getUnits().size())
                .sum();

        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
