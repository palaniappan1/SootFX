package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.jspecify.annotations.NonNull;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.types.ArrayType;
import sootup.core.types.ClassType;
import sootup.core.types.Type;
import sootup.core.views.View;


public class MethodParamIsInterface implements MethodFEU<Boolean> {
    @NonNull
    private final View view;

    public MethodParamIsInterface(View view) {
        this.view = view;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        if(target.getParameterCount()>0) {
            ClassType ct = null;
            for (Type t : target.getParameterTypes()) {
                if (t instanceof ClassType) {
                    ct = (ClassType) t;
                } else if (t instanceof ArrayType) {
                    Type base = ((ArrayType) t).getBaseType();
                    if (base instanceof ClassType) {
                        ct = (ClassType) base;
                    }
                }

                if (ct != null && view.getClass(ct).isPresent()) {
                    SootClass sc = view.getClass(ct).get();
                    return new Feature<>(getName(), sc.isInterface());
                }
            }
        }
        return new Feature<>(getName(), false);
    }
}
