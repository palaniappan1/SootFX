package manager;

import api.FeatureDescription;
import api.FeatureResource;
import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import core.rm.MethodFeatureSet;
import org.jspecify.annotations.NonNull;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.views.View;

import java.util.*;
import java.util.stream.Collectors;

import static manager.WholeProgramFX.getResourcePath;

public class MethodFX implements MultiInstanceFX<MethodFeatureSet, MethodFEU> {

    private static final Set<String> NEED_VIEW = new HashSet<>(Set.of("IsMethodClassAbstract",
            "IsMethodClassFinal", "IsMethodClassInnerClass", "IsMethodThreadRun",
            "MethodClassAccessModifier", "MethodParamIsInterface", "MethodCallsMethod"));


    @NonNull
    private final View view;

    public MethodFX(@NonNull View view) {
        this.view = view;
    }

    @Override
    public Set<MethodFeatureSet> getFeatures(Set<MethodFEU> featureExtractors) {
        Set<MethodFeatureSet> methodFeatureSets = new HashSet<>();
        Set<SootMethod> methods = new HashSet<>();
        Set<SootClass> classes = new HashSet<>();
        Set<SootClass> sootClasses = view.getClasses().collect(Collectors.toSet());
        sootClasses.stream().filter(FxUtil::isAppClass).forEach(classes::add);
        for(SootClass sc: classes){
            methods.addAll(sc.getMethods());
        }
        // callgraph didn't have app methods
        /*Iterator<Edge> cgIter = Scene.v().getCallGraph().iterator();
        while (cgIter.hasNext()){
            Edge edge = cgIter.next();
            methods.add(edge.src());
            methods.add(edge.tgt());
        }*/
        for(SootMethod method: methods){
            methodFeatureSets.add(extractMethodFeature(method, featureExtractors));
        }
        return methodFeatureSets;
    }

    @Override
    public Set<MethodFeatureSet> getAllFeatures(List<FeatureResource> featureResources) {
        List<FeatureDescription> features = FxUtil.listAllMethodFeatures();
        List<String> names = features.stream().map(f -> f.getName()).collect(Collectors.toList());
        return getFeatures(names, featureResources);
    }

    @Override
    public Set<MethodFeatureSet> getAllFeaturesExclude(Set<String> exclusion, List<FeatureResource> featureResources) {
        List<FeatureDescription> features = FxUtil.listAllMethodFeatures();
        List<String> names = features.stream().map(f -> f.getName()).collect(Collectors.toList());
        names.removeAll(exclusion);
        return getFeatures(names, featureResources);
    }

    @Override
    public Set<MethodFeatureSet> getFeatures(List<String> featureExtractors, List<FeatureResource> featureResources) {
        Set<MethodFEU> fxSet = new HashSet<>();
        for(String str: featureExtractors){
            Class<?> cls = null;
            MethodFEU newInstance = null;
            try{
                cls = Class.forName("core.fx.methodbased." + str);

                Optional<FeatureResource> featureResource = getResourcePath(featureResources, str);
                if (List.of("MethodReturnTypeEquals", "MethodCallsMethod").contains(str) && featureResource.isPresent()) {
                    String featureValue = featureResource.get().getFeatureValue();
                    newInstance = (MethodFEU) cls.getConstructor(String.class, View.class).newInstance(featureValue, view);
                }
                else if(featureResource.isPresent()){
                    String featureValue = featureResource.get().getFeatureValue();
                    newInstance = (MethodFEU) cls.getConstructor(String.class).newInstance(featureValue);
                }
                else if(NEED_VIEW.stream().anyMatch(s -> s.equalsIgnoreCase(str))){
                    newInstance = (MethodFEU) cls.getConstructor(View.class).newInstance(view);
                }
                else {
                    newInstance = (MethodFEU) cls.newInstance();
                }
            } catch (InstantiationException e){
                //System.out.println("ignoring feature that takes an input value:" + str);
            } catch (Exception e){
                System.err.println("feature not found:" + str);
            }
            if(newInstance!=null){
                fxSet.add(newInstance);
            }
        }
        return getFeatures(fxSet);
    }

    private static MethodFeatureSet extractMethodFeature(SootMethod sootMethod, Set<MethodFEU> featureExtractors){
        MethodFeatureSet rm = new MethodFeatureSet(sootMethod);
        for (MethodFEU<?> featureExtractor : featureExtractors) {
            Feature feature = featureExtractor.extract(sootMethod);
            rm.addFeature(feature);
        }
        return rm;
    }
}
