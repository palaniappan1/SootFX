package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

public class WholeProgramMethodCount implements WholeProgramFEU<Long> {
  @NonNull private final View view;

  public WholeProgramMethodCount(View view) {
    this.view = view;
  }

  @Override
  public Feature<Long> extract(CallGraph target) {
    Set<MethodSignature> methodSignatures = target.getMethodSignatures();
    long methodCount = methodSignatures.size();
    return new Feature<>(this.getClass().getSimpleName(), methodCount);
  }
}
