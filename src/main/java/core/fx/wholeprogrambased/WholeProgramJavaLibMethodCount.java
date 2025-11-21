package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

public class WholeProgramJavaLibMethodCount implements WholeProgramFEU<Long> {

  @NonNull private final View view;

  public WholeProgramJavaLibMethodCount(View view) {
    this.view = view;
  }

  @Override
  public Feature<Long> extract(CallGraph target) {
    Set<MethodSignature> methodSignatures = target.getMethodSignatures();
    Set<SootMethod> methods = new HashSet<>();
    methodSignatures.forEach(e -> view.getMethod(e).ifPresent(methods::add));

//    Set<MethodSignature> libraryMethodSignatures =
//        methodSignatures.stream()
//            .filter(e -> isJavaLibraryClass(e.getDeclClassType().toString()))
//            .collect(Collectors.toSet());

    long methodCount =
        methods.stream()
            .filter(
                m ->
                    view.getClass(m.getDeclaringClassType())
                        .map(SootClass::isLibraryClass)
                        .orElse(false))
            .collect(Collectors.toSet())
            .size();
    return new Feature<>(this.getClass().getSimpleName(), methodCount);
  }

//  public boolean isJavaLibraryClass(String className) {
//    return className.startsWith("java.")
//        || className.startsWith("sun.")
//        || className.startsWith("javax.")
//        || className.startsWith("com.sun.")
//        || className.startsWith("org.omg.")
//        || className.startsWith("org.xml.")
//        || className.startsWith("org.w3c.dom");
//  }
}
