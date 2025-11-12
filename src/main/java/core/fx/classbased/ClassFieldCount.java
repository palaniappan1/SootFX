package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import sootup.core.model.SootClass;

public class ClassFieldCount implements ClassFEU<Integer> {

  @Override
  public Feature<Integer> extract(SootClass target) {
    return new Feature<>(getName(), target.getFields().size());
  }
}
