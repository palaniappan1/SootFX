package core.fx.methodbased;

import core.fx.base.Feature;
import soot.SootMethod;

public class MethodReflectiveCalls extends MethodAPICount{
    @Override
    public Feature<Long> extractCountFromMethod(SootMethod method) {
        return new Feature<>(this.getClass().getSimpleName(), numberOfReflectiveCall);
    }
}
