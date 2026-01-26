package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.Iterator;

public class WholeProgramCallGraphEdges implements WholeProgramFEU<Long> {

    @Override
    public Feature<Long> extract(CallGraph cg) {
        Iterator<Edge> iterator = cg.iterator();
        long edgeCount = 0;
        while (iterator.hasNext()) {
            iterator.next();
            edgeCount++;
        }
        return new Feature<>(this.getClass().getSimpleName(), edgeCount);
    }
}
