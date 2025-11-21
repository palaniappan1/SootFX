package manager;

import static manager.WholeProgramFX.getResourcePath;

import api.FeatureDescription;
import api.FeatureResource;
import core.fx.FxUtil;
import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import core.rm.ClassFeatureSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import sootup.core.model.SootClass;
import sootup.core.views.View;

public class ClassFX implements MultiInstanceFX<ClassFeatureSet, ClassFEU> {

  @NonNull private final View view;

  public ClassFX(@NonNull View view) {
    this.view = view;
  }

  @Override
  public Set<ClassFeatureSet> getFeatures(Set<ClassFEU> featureExtractors) {
    Set<ClassFeatureSet> classFeatures = new HashSet<>();
    Set<SootClass> classes = new HashSet<>();
    Set<SootClass> sootClasses = view.getClasses().collect(Collectors.toSet());
    sootClasses.stream().filter(FxUtil::isAppClass).forEach(classes::add);
    for (SootClass sc : classes) {
      classFeatures.add(extractClassFeature(sc, featureExtractors));
    }
    return classFeatures;
  }

  @Override
  public Set<ClassFeatureSet> getAllFeatures(List<FeatureResource> featureResources) {
    List<FeatureDescription> features = FxUtil.listAllClassFeatures();
    List<String> names = features.stream().map(f -> f.getName()).collect(Collectors.toList());
    return getFeatures(names, featureResources);
  }

  @Override
  public Set<ClassFeatureSet> getAllFeaturesExclude(
      Set<String> exclusion, List<FeatureResource> featureResources) {
    List<FeatureDescription> features = FxUtil.listAllClassFeatures();
    List<String> names = features.stream().map(f -> f.getName()).collect(Collectors.toList());
    names.removeAll(exclusion);
    return getFeatures(names, featureResources);
  }

  @Override
  public Set<ClassFeatureSet> getFeatures(
      List<String> featureExtractors, List<FeatureResource> featureResources) {
    Set<ClassFEU> fxSet = new HashSet<>();
    for (String str : featureExtractors) {
      Class<?> cls = null;
      ClassFEU newInstance = null;
      try {
        Optional<FeatureResource> featureResource = Optional.empty();
        cls = Class.forName("core.fx.classbased." + str);
        if (featureResources != null && featureResources.isEmpty()) {
          featureResource = getResourcePath(featureResources, str);
        }
        if (featureResource.isPresent()) {
          newInstance =
              (ClassFEU)
                  cls.getConstructor(String.class)
                      .newInstance(featureResource.get().getFeatureValue());
        } else {
          newInstance = (ClassFEU) cls.newInstance();
        }
      } catch (InstantiationException e) {
        // System.out.println("ignoring feature that takes an input value:" + str);
      } catch (Exception e) {
        System.err.println("feature not found:" + str);
      }
      if (newInstance != null) {
        fxSet.add(newInstance);
      }
    }
    return getFeatures(fxSet);
  }

  private ClassFeatureSet extractClassFeature(
      SootClass sootClass, Set<ClassFEU> featureExtractors) {
    ClassFeatureSet rm = new ClassFeatureSet(sootClass);
    for (ClassFEU<?> featureExtractor : featureExtractors) {
      Feature feature = featureExtractor.extract(sootClass);
      rm.addFeature(feature);
    }
    return rm;
  }
}
