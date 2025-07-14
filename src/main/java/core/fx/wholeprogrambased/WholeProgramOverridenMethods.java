package core.fx.wholeprogrambased;

import core.fx.FxUtil;
import core.fx.base.Feature;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;

public class WholeProgramOverridenMethods extends WholeProgramMethodBasedFEU<Long>{
    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream()
                .filter(FxUtil::isOverriden)
                .count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
