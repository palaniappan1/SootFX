package core.fx.wholeprogrambased;

import com.google.common.collect.Streams;
import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/*
This class gives all the crypto API calls present in the program
If the target of the call is any crypto API given in the ${FxUtil.isCryptoPackage) then we will add one to the list
 */

public class WholeProgramCryptoAPICallCount implements WholeProgramFEU<Long> {

    @Override
    public Feature<Long> extract(CallGraph target) {
        Iterator<Edge> iterator = target.iterator();
        Set<SootMethod> methods = new HashSet<>();
        Stream<Edge> stream = Streams.stream(iterator);
        stream.forEach(edge -> {
            if(FxUtil.isCryptoPackage(edge.tgt().getDeclaringClass().getPackageName())){
                // This methods list shows all the methods from where the crypto calls are made
                // The design decision here is that we should know the number of methods which has any crypto calls
                methods.add(edge.src());
            }
        });
        long methodCount = methods.size();
        return new Feature<>(this.getClass().getSimpleName(), methodCount);
    }
}
