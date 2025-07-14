package core.fx.methodbased;

import core.fx.base.Feature;
import resource.APICallStats;
import soot.SootMethod;

public class MethodSourceStmts extends MethodAPICount {

    @Override
    public Feature<Long> extractCountFromMethod(SootMethod method, APICallStats stats) {
        System.out.println("Source Stmt count called for method " + method + " with the stats as " + stats);
        System.out.println("Value we have for source is " + stats.getSource());
        return new Feature<>(this.getClass().getSimpleName(), stats.getSource());
    }
}
