package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import soot.SootClass;

public class ClassContainsMethod implements ClassFEU<Boolean> {

    private String value;

    public ClassContainsMethod(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(SootClass target) {
        boolean containsMethod = target.getMethods().stream().anyMatch(sootMethod -> sootMethod.getName().contains(value));
        return new Feature<>(getName(), containsMethod);
    }

}