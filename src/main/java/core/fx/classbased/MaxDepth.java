package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import soot.SootClass;

public class MaxDepth implements ClassFEU<Integer> {
    @Override
    public Feature<Integer> extract(SootClass target) {
        int depth = 0;
        SootClass currentClass = target;
        while(currentClass.hasSuperclass()){
            currentClass = currentClass.getSuperclass();
            depth++;
        }
        return new Feature<>(getName(), depth);
    }
}
