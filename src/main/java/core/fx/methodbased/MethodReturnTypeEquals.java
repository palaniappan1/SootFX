package core.fx.methodbased;

import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.apache.commons.lang3.Strings;
import sootup.core.model.SootMethod;
import sootup.core.views.View;

public class MethodReturnTypeEquals implements MethodFEU<Boolean> {

  private String value;

  private View view;

  public MethodReturnTypeEquals(String value, View view) {
    this.value = value;
    this.view = view;
  }

  @Override
  public Feature<Boolean> extract(SootMethod target) {
    boolean equals = false;
    if (Strings.CI.equals(target.getReturnType().toString(), value)) {
      equals = true;
    }

    if (FxUtil.isOfType(view, target.getReturnType(), value)) {
      equals = true;
    }

    return new Feature<>(getName(value), equals);
  }
}
