package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;

/*
Find the number of traps present in the method referenced in the call graph
 */


public class WholeProgramTryCatchCount extends WholeProgramMethodBasedFEU<Long>{
    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream()
                .filter(SootMethod::hasActiveBody)
                .map(SootMethod::getActiveBody)
                .mapToLong(body -> body.getTraps().size())
                .sum();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
