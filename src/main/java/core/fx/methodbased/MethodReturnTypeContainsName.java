package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.apache.commons.lang3.Strings;
import sootup.core.model.SootMethod;

public class MethodReturnTypeContainsName implements MethodFEU<Boolean> {

  String value;

  public MethodReturnTypeContainsName(String value) {
    this.value = value;
  }

  @Override
  public Feature<Boolean> extract(SootMethod target) {
    return new Feature<>(
        getName(value), Strings.CI.contains(target.getReturnType().toString(), value));
  }
}
