package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import core.fx.FxUtil;
import sootup.core.model.SootMethod;
import org.jspecify.annotations.NonNull;
import sootup.core.views.View;


public class IsMethodThreadRun implements MethodFEU<Boolean> {

    @NonNull
    private final View view;

    public IsMethodThreadRun(View view) {
        this.view = view;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean isThreadRun = false;
        if (target.getName().equals("run")) {
            try {
                if (FxUtil.isOfType(view, target.getDeclaringClassType(), "java.lang.Runnable")) {
                    isThreadRun = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Feature<>(getName(), isThreadRun);
    }
}
