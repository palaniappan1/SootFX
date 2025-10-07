package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;

public class MethodParameterCount implements MethodFEU<Integer> {
    @Override
    public Feature<Integer> extract(SootMethod target) {
        return new Feature<>(getName(), target.getParameterCount());
    }
}
