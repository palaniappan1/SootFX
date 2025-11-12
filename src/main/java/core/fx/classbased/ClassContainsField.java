package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import sootup.core.model.SootClass;

public class ClassContainsField implements ClassFEU<Boolean> {

  private String value;

  public ClassContainsField(String value) {
    this.value = value;
  }

  @Override
  public Feature<Boolean> extract(SootClass target) {
    Boolean containsField =
        target.getFields().stream().anyMatch(field -> field.getName().contains(value));
    return new Feature<>(getName(), containsField);
  }
}
