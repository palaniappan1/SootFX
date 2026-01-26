package core.fx.classbased;

import core.fx.FxUtil;
import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import soot.SootClass;
import soot.SootMethod;

import java.util.ArrayList;
import java.util.List;

public class ClassLifecycleMethodCount implements ClassFEU<Integer> {

    @Override
    public Feature<Integer> extract(SootClass target) {
        List<SootMethod> lifecycleMethods = new ArrayList<>();
        List<SootMethod> sootMethodList = target.getMethods();
        for(SootMethod method : sootMethodList){
            String methodName = method.getName();
            if (FxUtil.isLifeCycleMethod(methodName)){
                lifecycleMethods.add(method);
            }
        }
        return new Feature<>(getName(), lifecycleMethods.size());
    }

}