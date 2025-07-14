package core.fx.methodbased;

import core.fx.base.Feature;
import resource.APICallStats;
import soot.SootMethod;

public class MethodTryCatchBlocks extends MethodAPICount {

    @Override
    public Feature<Long> extractCountFromMethod(SootMethod method, APICallStats stats) {
        return new Feature<>(this.getClass().getSimpleName(), stats.getTraps());
    }
}
