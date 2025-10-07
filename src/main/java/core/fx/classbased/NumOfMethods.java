package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import soot.SootClass;

public class NumOfMethods  implements ClassFEU<Integer> {

    @Override
    public Feature<Integer> extract(SootClass target) {
        return new Feature<>(getName(), target.getMethods().size());
    }
}
