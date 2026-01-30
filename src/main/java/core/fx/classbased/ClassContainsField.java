package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import fj.test.Bool;
import soot.SootClass;

public class ClassContainsField implements ClassFEU<Boolean> {

    private String value;

    public ClassContainsField(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(SootClass target) {
        boolean containsField = target.getFields().stream().anyMatch(sootField -> sootField.getName().contains(value));
        return new Feature<>(getName(), containsField);
    }

}