package manager;

import api.FeatureDescription;
import api.FeatureResource;
import core.fx.FxUtil;
import core.fx.base.WholeProgramFEU;
import core.rm.WholeProgramFeatureSet;
import java.util.*;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import sootup.callgraph.CallGraph;
import sootup.callgraph.ClassHierarchyAnalysisAlgorithm;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.views.View;

public class WholeProgramFX implements SingleInstanceFX<WholeProgramFeatureSet, WholeProgramFEU> {

  @NonNull private final View view;

  public WholeProgramFX(View view) {
    this.view = view;
  }

  @Override
  public WholeProgramFeatureSet getFeatures(Set<WholeProgramFEU> featureExtractors) {
    WholeProgramFeatureSet wholeProgramFeature = new WholeProgramFeatureSet();
    Set<SootClass> classes = new HashSet<>();

    Set<SootClass> sootClasses = view.getClasses().collect(Collectors.toSet());
    sootClasses.stream().filter(FxUtil::isAppClass).forEach(classes::add);
    Set<SootMethod> publicMethods =
        classes.stream()
            .flatMap(cls -> cls.getMethods().stream().filter(m -> m.isPublic() && m.isConcrete()))
            .collect(Collectors.toSet());
    ClassHierarchyAnalysisAlgorithm cha = new ClassHierarchyAnalysisAlgorithm(view);

    List<MethodSignature> methodSignatures = new ArrayList<>();
    publicMethods.forEach(m -> methodSignatures.add(m.getSignature()));
    CallGraph cg = cha.initialize(methodSignatures);

    for (WholeProgramFEU<?> featureExtractor : featureExtractors) {
      wholeProgramFeature.addFeature(featureExtractor.extract(cg));
    }
    return wholeProgramFeature;
  }

  @Override
  public WholeProgramFeatureSet getAllFeatures(List<FeatureResource> featureResources) {
    List<FeatureDescription> features = FxUtil.listAllWholeProgramFeatures();
    List<String> names = features.stream().map(f -> f.getName()).collect(Collectors.toList());
    return getFeatures(names, featureResources);
  }

  @Override
  public WholeProgramFeatureSet getAllFeaturesExclude(
      Set<String> exclusion, List<FeatureResource> featureResources) {
    List<FeatureDescription> features = FxUtil.listAllWholeProgramFeatures();
    List<String> names = features.stream().map(f -> f.getName()).collect(Collectors.toList());
    names.removeAll(exclusion);
    return getFeatures(names, featureResources);
  }

  @Override
  public WholeProgramFeatureSet getFeatures(
      List<String> featureExtractors, List<FeatureResource> featureResources) {
    Set<WholeProgramFEU> fxSet = new HashSet<>();
    for (String str : featureExtractors) {
      Class<?> cls = null;
      WholeProgramFEU newInstance = null;
      try {
        cls = Class.forName("core.fx.wholeprogrambased." + str);
        Optional<FeatureResource> featureResource = Optional.empty();
        if (featureResources != null && featureResources.isEmpty()) {
          featureResource = getResourcePath(featureResources, str);
        }
        if (featureResource.isPresent()) {
          String featureValue = featureResource.get().getFeatureValue();
          newInstance =
              (WholeProgramFEU)
                  cls.getConstructor(String.class, View.class).newInstance(featureValue, view);
        } else {
          newInstance = (WholeProgramFEU) cls.getConstructor(View.class).newInstance(view);
        }
      } catch (InstantiationException e) {
        //  System.out.println("ignoring feature that takes an input value:" + str);
      } catch (Exception e) {
        System.err.println("feature not found:" + str);
      }
      if (newInstance != null) {
        fxSet.add(newInstance);
      }
    }
    return getFeatures(fxSet);
  }

  public static Optional<FeatureResource> getResourcePath(
      List<FeatureResource> featureResources, String featureName) {
    Optional<FeatureResource> first =
        featureResources.stream()
            .filter(featureResource -> featureResource.getFeatureName().equals(featureName))
            .findFirst();
    return first;
  }
}
