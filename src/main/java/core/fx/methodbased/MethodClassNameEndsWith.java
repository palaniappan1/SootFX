package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.apache.commons.lang3.Strings;
import sootup.core.model.SootMethod;

public class MethodClassNameEndsWith implements MethodFEU<Boolean> {

    private String value;

    public MethodClassNameEndsWith(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        return new Feature<>(getName(value), Strings.CI.contains(target.getDeclaringClassType().getClassName(), value));
    }
}
