package core.fx.methodbased;

import core.fx.base.Feature;
import resource.APICallStats;
import soot.SootMethod;

public class MethodReflectiveCalls extends MethodAPICount{
    @Override
    public Feature<Boolean> extractCountFromMethod(SootMethod method, APICallStats stats) {
        return new Feature<>(this.getClass().getSimpleName(), stats.getReflection());
    }
}
