package core.fx.wholeprogrambased;

import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import org.jspecify.annotations.NonNull;
import resource.FileConnector;
import sootup.callgraph.CallGraph;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

import java.util.*;

public class WholeProgramAPICallCount implements WholeProgramFEU<Map<String, Long>> {

    private List<String> methodSignatures;

    @NonNull
    private final View view;

    public WholeProgramAPICallCount(String resourcePath, View view) {
        this.methodSignatures = FileConnector.getMethodSignatures(resourcePath);
        this.view = view;
    }

    @Override
    public Feature<Map<String, Long>> extract(CallGraph target) {
        Set<MethodSignature> ms = target.getMethodSignatures();
        Map<String, Long> methodCount = new HashMap<>();
        ms.forEach(e-> {
            if(methodSignatures.contains(e.toString())){
                if(methodCount.containsKey(e.toString())){
                    Long count = methodCount.get(e.toString());
                    methodCount.put(e.toString(), ++count);
                }else{
                    methodCount.put(e.toString(), 1L);
                }
            }
        });
        return new Feature<>(this.getClass().getSimpleName(), methodCount);
    }
}