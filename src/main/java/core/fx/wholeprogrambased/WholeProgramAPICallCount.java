package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import java.util.*;
import org.jspecify.annotations.NonNull;
import resource.FileConnector;
import sootup.callgraph.CallGraph;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

public class WholeProgramAPICallCount implements WholeProgramFEU<Map<String, Long>> {

  private List<String> methodSignatures;

  @NonNull private final View view;

  public WholeProgramAPICallCount(String resourcePath, View view) {
    this.methodSignatures = FileConnector.getMethodSignatures(resourcePath);
    this.view = view;
  }

  @Override
  public Feature<Map<String, Long>> extract(CallGraph target) {
    Set<MethodSignature> sootMethodSignatures = target.getMethodSignatures();
    Map<String, Long> methodCount = new HashMap<>();
    sootMethodSignatures.forEach(
        e -> {
          if (methodSignatures.contains(e.toString())) {
            Long count = methodCount.getOrDefault(e.toString(), 0L);
            methodCount.put(e.toString(), count + 1);
          }
        });
    return new Feature<>(this.getClass().getSimpleName(), methodCount);
  }
}
