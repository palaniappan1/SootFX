package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import soot.SootMethod;

public class IsMethodVirtual implements MethodFEU<Boolean> {
    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean isVirtual = target.isConcrete() // has body
                && !target.isStatic() // not static
                && !target.isPrivate() //not private
                && !target.isConstructor() // not a constructor
                && !target.isFinal(); // not final
        return new Feature<>(getName(), isVirtual);
    }
}
