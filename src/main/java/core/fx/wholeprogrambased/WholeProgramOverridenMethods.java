package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;

public class WholeProgramOverridenMethods extends WholeProgramMethodBasedFEU<Long>{
    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream()
                .filter(this::isOverriden)
                .count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }

    public boolean isOverriden(SootMethod method){
        if(method.isPrivate() || method.isStatic() || method.isFinal() || method.isConstructor()){ // Cannot be overriden
            return false;
        }

        SootClass declaringClass = method.getDeclaringClass();
        String subSignature = method.getSubSignature();
        for(SootClass sootClass : Scene.v().getActiveHierarchy().getSubclassesOf(declaringClass)){
            if(sootClass.declaresMethod(subSignature)){
                return true;
            }
        }
        return false;
    }
}
