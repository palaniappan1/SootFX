package core.fx.methodbased;

import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;

public class IsAppMethod implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        return new Feature<>(getName(), FxUtil.isAppMethod(target));
    }
}
