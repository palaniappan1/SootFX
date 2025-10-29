package core.fx.wholeprogrambased;

import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.views.View;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * all methods from scene
 */
public class WholeProgramAllAppMethodCount implements WholeProgramFEU<Long> {

    @NonNull
    private final View view;

    public WholeProgramAllAppMethodCount(@NonNull View view) {
        this.view = view;
    }

    @Override
    public Feature<Long> extract(CallGraph target) {
        Set<SootMethod> methods = new HashSet<>();
        Set<SootClass> classes = new HashSet<>();
        Set<SootClass> sootClasses =  view.getClasses().collect(Collectors.toSet());
        sootClasses.stream().filter(FxUtil::isAppClass).forEach(classes::add);
        for(SootClass sc: classes){
            methods.addAll(sc.getMethods());
        }
        long methodCount = methods.size();
        return new Feature<>(this.getClass().getSimpleName(), methodCount);
    }

}