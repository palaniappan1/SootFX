package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

import java.util.HashSet;
import java.util.Set;

public class WholeProgramJavaLibMethodCount implements WholeProgramFEU<Long> {

    @NonNull
    private final View view;

    public WholeProgramJavaLibMethodCount(View view) {
        this.view = view;
    }

    @Override
    public Feature<Long> extract(CallGraph target) {
        Set<MethodSignature> methodSignatures = target.getMethodSignatures();
        Set<SootMethod> methods = new HashSet<>();

        methodSignatures.forEach(e -> view.getMethod(e).ifPresent(methods::add));

        methods.stream().filter(m -> view.getClass(m.getDeclaringClassType())
                .map(SootClass::isLibraryClass).orElse(false)).forEach(methods::add);

        return new Feature<>(this.getClass().getSimpleName(), (long) methods.size());
    }
}