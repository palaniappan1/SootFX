package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.jspecify.annotations.NonNull;
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
            ClassType classType = null;
            for (Type type : target.getParameterTypes()) {
                if (type instanceof ClassType) {
                    classType = (ClassType) type;
                } else if (type instanceof ArrayType) {
                    Type base = ((ArrayType) type).getBaseType();
                    if (base instanceof ClassType) {
                        classType = (ClassType) base;
                    }
                }
                if (classType != null && view.getTypeHierarchy().contains(classType)) {
                    return new Feature<>(getName(), view.getTypeHierarchy().isInterface(classType));
                }
                if(classType !=null) {
                    String name = classType.toString();
                    Boolean viaReflection = isInterface(name);
                    if (viaReflection != null && viaReflection) {
                        return new Feature<>(getName(), true);
                    }
                }
            }
        }
        return new Feature<>(getName(), false);
    }

    private static Boolean isInterface(String fqName) {
        if (!fqName.startsWith("java.")) return null;
        try {
            Class<?> c = Class.forName(fqName, false, ClassLoader.getSystemClassLoader());
            return c.isInterface();
        } catch (Throwable ignore) {
            return null;
        }
    }
}
