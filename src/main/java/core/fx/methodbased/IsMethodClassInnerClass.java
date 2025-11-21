package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.jspecify.annotations.NonNull;
import sootup.core.model.SootMethod;
import sootup.core.views.View;

public class IsMethodClassInnerClass implements MethodFEU<Boolean> {

  @NonNull private final View view;

  public IsMethodClassInnerClass(View view) {
    this.view = view;
  }

  @Override
  public Feature<Boolean> extract(SootMethod target) {
    var clsOpt = view.getClass(target.getDeclaringClassType());
    boolean hasOuterClass = clsOpt.isPresent() && clsOpt.get().hasOuterClass();
    return new Feature<>(getName(), hasOuterClass);
  }
}
