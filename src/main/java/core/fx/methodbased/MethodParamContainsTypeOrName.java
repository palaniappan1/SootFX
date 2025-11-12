package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.apache.commons.lang3.Strings;
import sootup.core.model.SootMethod;
import sootup.core.types.Type;

public class MethodParamContainsTypeOrName implements MethodFEU<Boolean> {

  String value;

  public MethodParamContainsTypeOrName(String value) {
    this.value = value;
  }

  @Override
  public Feature<Boolean> extract(SootMethod target) {
    if (target.getParameterCount() > 0) {
      for (Type type : target.getParameterTypes()) {
        if (Strings.CI.contains(type.toString(), value)) {
          return new Feature<>(getName(value), true);
        }
      }
    }
    return new Feature<>(getName(value), false);
  }
}
