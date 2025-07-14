package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;
import soot.Unit;

public class MethodBranchCount implements MethodFEU<Long> {

    @Override
    public Feature<Long> extract(SootMethod method) {
        long count = -1L;

        if(method.hasActiveBody()){
            count = method.getActiveBody().getUnits().stream().filter(Unit::branches).count();
        }
        return new Feature<>(getName(), count);
    }
}
