package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;

/*
Count the number of interfaces referenced by methods in the call graph
 */


public class WholeProgramInterfaceCount extends WholeProgramMethodBasedFEU<Long> {

    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream().map(SootMethod::getDeclaringClass).distinct().filter(SootClass::isInterface).count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
