package core.fx.methodbased;

import core.fx.base.Feature;
import resource.APICallStats;
import soot.SootMethod;

public class MethodSinkStmts extends MethodAPICount {

    @Override
    public Feature<Long> extractCountFromMethod(SootMethod method, APICallStats stats) {
        System.out.println("sink Stmt count called for method " + method + " with the stats as " + stats);
        System.out.println("Value we have for sink is " + stats.getSink());
        return new Feature<>(this.getClass().getSimpleName(), stats.getSink());
    }
}
