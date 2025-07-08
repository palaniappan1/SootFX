package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;

public class WholeProgramBranchesCount extends WholeProgramMethodBasedFEU<Long> {
    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream()
                .filter(SootMethod::hasActiveBody)
                .flatMap(m -> m.getActiveBody().getUnits().stream())
                .filter(this::isBranchingUnit)
                .count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
