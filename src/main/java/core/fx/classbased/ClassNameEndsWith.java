package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import org.apache.commons.lang3.Strings;
import sootup.core.model.SootClass;

public class ClassNameEndsWith implements ClassFEU<Boolean> {

  String value;

  public ClassNameEndsWith(String value) {
    this.value = value;
  }

  @Override
  public Feature<Boolean> extract(SootClass target) {
    return new Feature<>(getName(value), Strings.CI.endsWith(target.getName(), value));
  }
}
