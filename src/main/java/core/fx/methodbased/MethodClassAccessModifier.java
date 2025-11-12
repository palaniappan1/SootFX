package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.jspecify.annotations.NonNull;
import sootup.core.model.SootMethod;
import sootup.core.views.View;

public class MethodClassAccessModifier implements MethodFEU<String> {

  @NonNull private final View view;

  public MethodClassAccessModifier(@NonNull View view) {
    this.view = view;
  }

  @Override
  public Feature<String> extract(SootMethod target) {
    String modifier = "UNK";
    if (view.getClass(target.getDeclClassType()).isEmpty()) {
      throw new IllegalStateException("Class not found: " + target.getDeclClassType());
    }
    if (view.getClass(target.getDeclClassType()).get().isPublic()) {
      modifier = "public";
    } else if (view.getClass(target.getDeclClassType()).get().isProtected()) {
      modifier = "protected";
    } else if (view.getClass(target.getDeclClassType()).get().isPrivate()) {
      modifier = "private";
    }
    return new Feature<>(getName(), modifier);
  }
}
