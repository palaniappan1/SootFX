package core.fx.wholeprogrambased;

import com.google.common.collect.Streams;
import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.*;
import java.util.stream.Stream;

public class WholeProgramFileHandlingCount implements WholeProgramFEU<Long> {

    @Override
    public Feature<Long> extract(CallGraph target) {
        Iterator<Edge> iterator = target.iterator();
        Set<SootMethod> methods = new HashSet<>();
        Stream<Edge> stream = Streams.stream(iterator).filter(Objects::nonNull);
        stream.forEach(edge -> {
            if(edge.tgt() != null && FxUtil.isFileHandingPackage(edge.tgt().getDeclaringClass().getPackageName())){
                methods.add(edge.src());
            }
        });
        long methodCount = methods.size();
        return new Feature<>(this.getClass().getSimpleName(), methodCount);
    }
}
