package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Strings;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

public class WholeProgramContainsAPICall implements WholeProgramFEU<Boolean> {

  private String value;

  @NonNull private final View view;

  public WholeProgramContainsAPICall(String value, View view) {
    this.view = view;
    this.value = value;
  }

  @Override
  public Feature<Boolean> extract(CallGraph target) {
    Set<MethodSignature> methodSignatures = target.getMethodSignatures();
    Set<SootMethod> methods = new HashSet<>();
    methodSignatures.forEach(
        e ->
            view.getMethod(e)
                .filter(m -> Strings.CI.contains(m.getName(), value))
                .ifPresent(methods::add));

    return new Feature<>(this.getClass().getSimpleName(), !methods.isEmpty());
  }
}
