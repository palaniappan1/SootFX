package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.apache.commons.lang3.StringUtils;
import sootup.core.model.SootMethod;

public class MethodClassNameEndsWith implements MethodFEU<Boolean> {

    private String value;

    public MethodClassNameEndsWith(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        return new Feature<>(getName(value), StringUtils.endsWithIgnoreCase(target.getClass().getDeclaringClass().getName(), value));
    }
}
