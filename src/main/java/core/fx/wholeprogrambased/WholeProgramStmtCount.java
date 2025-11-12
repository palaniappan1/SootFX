package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

public class WholeProgramStmtCount implements WholeProgramFEU<Long> {

  @NonNull private final View view;

  public WholeProgramStmtCount(View view) {
    this.view = view;
  }

  @Override
  public Feature<Long> extract(CallGraph target) {
    Set<MethodSignature> methodSignatures = target.getMethodSignatures();
    Set<SootMethod> methods = new HashSet<>();
    methodSignatures.forEach(e -> view.getMethod(e).ifPresent(methods::add));

    long unitCount =
        methods.stream()
            .filter(SootMethod::hasBody)
            .map(SootMethod::getBody)
            .flatMap(body -> body.getStmts().stream())
            .collect(Collectors.toSet())
            .size();
    return new Feature<>(this.getClass().getSimpleName(), unitCount);
  }
}
