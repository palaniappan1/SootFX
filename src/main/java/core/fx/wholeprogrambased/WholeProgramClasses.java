package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Objects;
import java.util.Set;


/*
Counts the number of classes referenced by methods in the call graph
 */

public class WholeProgramClasses extends WholeProgramMethodBasedFEU<Long>{
    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream().filter(Objects::nonNull).map(SootMethod::getDeclaringClass).distinct().filter(SootClass::isConcrete).count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
