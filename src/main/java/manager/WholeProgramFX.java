package manager;

import api.FeatureDescription;
import api.FeatureResource;
import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.WholeProgramFEU;
import core.rm.WholeProgramFeatureSet;
import soot.Scene;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class WholeProgramFX implements SingleInstanceFX<WholeProgramFeatureSet, WholeProgramFEU> {

    @Override
    public WholeProgramFeatureSet getFeatures(Set<WholeProgramFEU> featureExtractors) {
        WholeProgramFeatureSet wholeProgramFeature = new WholeProgramFeatureSet();
        CallGraph cg = Scene.v().getCallGraph();
        ExecutorService executorService = Executors.newFixedThreadPool(featureExtractors.size());
        List<Callable<Void>> tasks = featureExtractors.stream().map(f -> (Callable<Void>) () -> {
            Feature feature = f.extract(cg);
            synchronized (wholeProgramFeature) {
                wholeProgramFeature.addFeature(feature);
            }
            return null;
        }).collect(Collectors.toList());
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
        return wholeProgramFeature;
    }

    @Override
    public WholeProgramFeatureSet getAllFeatures() {
        List<String> featureNames = FxUtil.listAllWholeProgramFeatures()
                .stream()
                .map(FeatureDescription::getName)
                .collect(Collectors.toList());
        return getFeatures(featureNames);
    }

    @Override
    public WholeProgramFeatureSet getAllFeaturesExclude(Set<String> exclusion) {
        List<String> featureNames = FxUtil.listAllWholeProgramFeatures()
                .stream()
                .map(FeatureDescription::getName)
                .filter(name -> !exclusion.contains(name))
                .collect(Collectors.toList());
        return getFeatures(featureNames);
    }

    @Override
    public WholeProgramFeatureSet getFeatures(List<String> featureExtractors) {
        return getFeatures(featureExtractors, Collections.emptyList());
    }

    @Override
    public WholeProgramFeatureSet getFeatures(List<String> featureExtractors, List<FeatureResource> featureResources) {
        Set<WholeProgramFEU> fxSet = featureExtractors.stream()
                .map(name -> instantiateFeature(name, featureResources))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        return getFeatures(fxSet);
    }

    private Optional<WholeProgramFEU> instantiateFeature(String featureName, List<FeatureResource> featureResources) {
        String className = "core.fx.wholeprogrambased." + featureName;
        try {
            Class<?> cls = Class.forName(className);
            Optional<FeatureResource> resource = featureResources.stream()
                    .filter(fr -> fr.getFeatureName().equals(featureName))
                    .findFirst();

            if (resource.isPresent()) {
                // Assume constructor with String parameter exists
                return Optional.of((WholeProgramFEU) cls.getConstructor(String.class).newInstance(resource.get().getResourcePath()));
            } else {
                return Optional.of((WholeProgramFEU) cls.getDeclaredConstructor().newInstance());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Feature class not found: " + className);
        } catch (ReflectiveOperationException e) {
            System.err.println("Failed to instantiate feature: " + className + " due to " + e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<FeatureResource> getResourcePath(List<FeatureResource> featureResources, String featureName) {
        return featureResources.stream().filter(featureResource ->
                featureResource.getFeatureName().equals(featureName)).findFirst();
    }

}
