package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

public class WholeProgramStaticMethodCount implements WholeProgramFEU<Long> {

  @NonNull private final View view;

  public WholeProgramStaticMethodCount(View view) {
    this.view = view;
  }

  @Override
  public Feature<Long> extract(CallGraph target) {
    Set<MethodSignature> methodSignatures = target.getMethodSignatures();
    Set<SootMethod> methods = new HashSet<>();
    methodSignatures.forEach(
        e -> view.getMethod(e).filter(SootMethod::isStatic).ifPresent(methods::add));
    long methodCount = methods.size();
    return new Feature<>(this.getClass().getSimpleName(), methodCount);
  }
}
