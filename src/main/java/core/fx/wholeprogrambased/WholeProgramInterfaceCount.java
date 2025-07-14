package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;
import java.util.stream.Collectors;

/*
Count the number of interfaces referenced by methods in the call graph
 */


public class WholeProgramInterfaceCount extends WholeProgramMethodBasedFEU<Long> {

    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        Set<SootClass> interfaces = methods.stream().map(SootMethod::getDeclaringClass).distinct().filter(SootClass::isInterface).collect(Collectors.toSet());
        return new Feature<>(this.getClass().getSimpleName(), (long) interfaces.size());
    }
}
