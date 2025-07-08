package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;

public class WholeProgramMethodCount extends WholeProgramMethodBasedFEU<Long> {

    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        return new Feature<>(this.getClass().getSimpleName(), (long) methods.size());
    }
}