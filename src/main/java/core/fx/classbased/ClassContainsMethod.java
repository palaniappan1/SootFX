package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import sootup.core.model.SootClass;

public class ClassContainsMethod implements ClassFEU<Boolean> {

  private String value;

  public ClassContainsMethod(String value) {
    this.value = value;
  }

  @Override
  public Feature<Boolean> extract(SootClass target) {
    Boolean containsMethod =
        target.getMethods().stream()
            .filter(sootMethod -> sootMethod.getName().contains(value))
            .findAny()
            .isPresent();
    return new Feature<>(getName(), containsMethod);
  }
}
