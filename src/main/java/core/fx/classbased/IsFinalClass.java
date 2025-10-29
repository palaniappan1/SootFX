package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import sootup.core.model.SootClass;

public class IsFinalClass implements ClassFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootClass target) {
        return new Feature<>(getName(), target.isFinal());
    }
}
