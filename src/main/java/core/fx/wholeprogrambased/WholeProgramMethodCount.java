package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import sootup.callgraph.CallGraph;
import sootup.core.signatures.MethodSignature;

import java.util.Set;

public class WholeProgramMethodCount implements WholeProgramFEU<Long> {

    @Override
    public Feature<Long> extract(CallGraph target) {
        Set<MethodSignature> methodSignatures = target.getMethodSignatures();
        long methodCount = methodSignatures.size();
        return new Feature<>(this.getClass().getSimpleName(), methodCount);
    }
}