package core.fx.classbased;

import core.fx.FxUtil;
import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import soot.SootClass;
import soot.SootMethod;

import java.util.ArrayList;
import java.util.List;

public class ClassEventListenersMethodCount implements ClassFEU<Integer> {

    @Override
    public Feature<Integer> extract(SootClass target) {
        List<SootMethod> eventListenersMethod = new ArrayList<>();
        List<SootMethod> sootMethodList = target.getMethods();
        for(SootMethod method : sootMethodList){
            System.out.println("Analyzing method: " + method.getName());
            String methodName = method.getName();
            if (FxUtil.isEventListenerMethod(methodName)){
                eventListenersMethod.add(method);
            }
        }
        return new Feature<>(getName(), eventListenersMethod.size());
    }

}
