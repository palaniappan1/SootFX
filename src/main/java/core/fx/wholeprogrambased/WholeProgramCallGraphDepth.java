package core.fx.wholeprogrambased;

import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.*;

public class WholeProgramCallGraphDepth implements WholeProgramFEU<Long> {

    long maxDepth = 0;
    private final Map<SootMethod, Integer> depthMemoization = new HashMap<>();

    @Override
    public Feature<Long> extract(CallGraph target) {
        List<SootMethod> entryPoints = FxUtil.getEntryPoints(Scene.v().getApplicationClasses());
        for(SootMethod method: entryPoints){
            computeMaxDepth(method, target, 0, new HashSet<>());
        }
        return new Feature<>(this.getName(), maxDepth);
    }

    private int computeMaxDepth(SootMethod method, CallGraph cg, int currentDepth, Set<SootMethod> visited) {
        if (visited.contains(method)) {
            return 0;
        }

        if(depthMemoization.containsKey(method)){
            return depthMemoization.get(method);
        }

        visited.add(method);
        int localMax = currentDepth;

        Iterator<Edge> edges = cg.edgesOutOf(method);
        while (edges.hasNext()) {
            SootMethod target = edges.next().getTgt().method();
            int depth = computeMaxDepth(target, cg, currentDepth + 1, visited);
            localMax = Math.max(localMax, depth);
        }

        visited.remove(method);
        depthMemoization.put(method, localMax);
        maxDepth = Math.max(maxDepth, localMax);
        return localMax;
    }
}
