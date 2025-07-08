package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.SwitchStmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class WholeProgramMethodBasedFEU<T> implements WholeProgramFEU<T> {

    private static Set<SootMethod> cachedMethods = null;

    @Override
    public Feature<T> extract(CallGraph target) {
        if (cachedMethods == null) {
            cachedMethods = computeMethods(target);
        }
        return extractWithMethods(target, cachedMethods);
    }

    protected abstract Feature<T> extractWithMethods(CallGraph cg, Set<SootMethod> methods);

    protected Set<SootMethod> computeMethods(CallGraph cg) {
        Set<SootMethod> methods = new HashSet<>();
        for (Edge edge : cg) {
            methods.add(edge.src());
            methods.add(edge.tgt());
        }
        return methods;
    }

    protected boolean isBranchingUnit(Unit unit) {
        return unit instanceof IfStmt || unit instanceof GotoStmt || unit instanceof SwitchStmt;
    }

    protected boolean isReflectiveCall(SootMethodRef sootMethodRef){
        String className = sootMethodRef.getDeclaringClass().getName();
        String methodName = sootMethodRef.getName();
        return (className.equals("java.lang.Class") && methodName.equals("forName")) ||
                (className.equals("java.lang.reflect.Method") && methodName.equals("invoke")) ||
                (className.equals("java.lang.reflect.Constructor") && methodName.equals("newInstance")) ||
                (className.equals("java.lang.Class") && methodName.equals("getMethod")) ||
                (className.equals("java.lang.Class") && methodName.equals("getDeclaredMethod")) ||
                (className.equals("java.lang.Class") && methodName.equals("getField")) ||
                (className.equals("java.lang.Class") && methodName.equals("getDeclaredField"));
    }

    // Allow clearing the static cache if needed
    public static void resetCache() {
        cachedMethods = null;
    }

}
