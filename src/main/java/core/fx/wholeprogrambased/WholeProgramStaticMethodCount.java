package core.fx.wholeprogrambased;

import com.google.common.collect.Streams;
import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/*
Gives all the count of the static methods present in the reachable methods from the call graph
 */

public class WholeProgramStaticMethodCount extends WholeProgramMethodBasedFEU<Long> {

    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream().filter(Objects::nonNull).filter(SootMethod::isStatic).count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}