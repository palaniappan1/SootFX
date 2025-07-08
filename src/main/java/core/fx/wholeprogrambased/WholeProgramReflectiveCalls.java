package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Set;

public class WholeProgramReflectiveCalls extends WholeProgramMethodBasedFEU<Long>{
    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = methods.stream()
                .filter(SootMethod::hasActiveBody)
                .flatMap(m -> m.getActiveBody().getUnits().stream())
                .filter(unit -> unit instanceof InvokeStmt)
                .map(unit -> ((InvokeStmt) unit).getInvokeExpr())
                .map(InvokeExpr::getMethodRef)
                .filter(this::isReflectiveCall)
                .count();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }
}
