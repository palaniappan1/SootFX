package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import java.util.List;
import org.apache.commons.lang3.Strings;
import sootup.core.model.SootMethod;
import sootup.core.types.Type;

public class MethodParamTypeMatchesReturnType implements MethodFEU<Boolean> {
  @Override
  public Feature<Boolean> extract(SootMethod target) {
    List<Type> paramList = target.getParameterTypes();
    for (Type param : paramList) {
      if (Strings.CI.contains(param.toString(), target.getReturnType().toString())) {
        return new Feature<>(getName(), true);
      }
    }
    return new Feature<>(getName(), false);
  }
}
