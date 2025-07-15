package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import resource.APICallStats;
import soot.SootMethod;

public class MethodTryCatchBlocks implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean trapsPresent = target.hasActiveBody() && !target.getActiveBody().getTraps().isEmpty();
        return new Feature<>(this.getClass().getSimpleName(), trapsPresent);
    }
}
