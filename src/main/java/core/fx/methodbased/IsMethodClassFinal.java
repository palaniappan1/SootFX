package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.jspecify.annotations.NonNull;
import sootup.core.model.SootMethod;
import sootup.core.views.View;

public class IsMethodClassFinal implements MethodFEU<Boolean> {

    @NonNull
    private final View view;

    public IsMethodClassFinal(View view) {
        this.view = view;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        var clsOpt = view.getClass(target.getDeclaringClassType());
        boolean isStatic = clsOpt.isPresent() && clsOpt.get().isStatic();
        return new Feature<>(getName(), isStatic);
    }

}
