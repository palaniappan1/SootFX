package api;

import core.fx.FxUtil;
import core.rm.*;
import manager.ClassFX;
import manager.MethodFX;
import manager.WholeProgramFX;
import resource.SootConnector;
import core.fx.base.*;
import sootup.core.views.View;

import java.io.*;
import java.util.*;

public class SootFX {

    private String mainClass;
    private List<String> classPaths;
    private boolean appOnly;
    private String androidJars;

    public static boolean isAPK = false;

    public SootFX addClassPath(String classPath) {
        if (this.classPaths == null) {
            this.classPaths = new ArrayList<>();
        }
        this.classPaths.add(classPath);
        return this;
    }

    public SootFX mainClass(String fullyQualifiedClassName) {
        this.mainClass = fullyQualifiedClassName;
        return this;
    }

    public SootFX androidJars(String androidJars) {
        this.androidJars = androidJars;
        isAPK = true;
        return this;
    }

    public SootFX appOnly() {
        appOnly = true;
        return this;
    }

    private void validate() {
        if (classPaths == null || classPaths.isEmpty()) {
            throw new RuntimeException("Class Path not set. call addClassPath(/class/path/to/target/program)");
        }
    }


    public Set<MethodFeatureSet> extractMethodFeaturesInclude(Set<MethodFEU> featureExtractors) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        //SootClass sc = Scene.v().forceResolve(mainClass, SootClass.BODIES);
        //Chain<SootClass> classes = Scene.v().getApplicationClasses();
        return new MethodFX(view).getFeatures(featureExtractors);
    }

    public Set<MethodFeatureSet> extractAllMethodFeatures(List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new MethodFX(view).getAllFeatures(featureResources);
    }

    public Set<MethodFeatureSet> extractMethodFeaturesExclude(Set<String> exclusion, List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new MethodFX(view).getAllFeaturesExclude(exclusion, featureResources);
    }

    public Set<MethodFeatureSet> extractMethodFeaturesInclude(List<String> featureExtractors, List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new MethodFX(view).getFeatures(featureExtractors, featureResources);
    }

    public List<FeatureDescription> listAllMethodFeatures(){
        return FxUtil.listAllMethodFeatures();
    }


    public Set<ClassFeatureSet> extractClassFeaturesInclude(Set<ClassFEU> featureExtractors) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new ClassFX(view).getFeatures(featureExtractors);
    }

    public Set<ClassFeatureSet> extractAllClassFeatures(List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new ClassFX(view).getAllFeatures(featureResources);
    }

    public Set<ClassFeatureSet> extractClassFeaturesExclude(Set<String> exclusion, List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new ClassFX(view).getAllFeaturesExclude(exclusion, featureResources);
    }

    public Set<ClassFeatureSet> extractClassFeaturesInclude(List<String> featureExtractors,  List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new ClassFX(view).getFeatures(featureExtractors, featureResources);
    }

    public WholeProgramFeatureSet extractWholeProgramFeaturesInclude(Set<WholeProgramFEU> featureExtractors) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new WholeProgramFX(view).getFeatures(featureExtractors);
    }

    public WholeProgramFeatureSet extractAllWholeProgramFeatures(List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new WholeProgramFX(view).getAllFeatures(featureResources);
    }

    public WholeProgramFeatureSet extractWholeProgramFeaturesExclude(Set<String> exclusion, List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new WholeProgramFX(view).getAllFeaturesExclude(exclusion, featureResources);
    }

    public WholeProgramFeatureSet extractWholeProgramFeaturesInclude(List<String> featureExtractors, List<FeatureResource> featureResources) {
        validate();
        View view = SootConnector.sootupConnector(mainClass, classPaths, appOnly, androidJars);
        return new WholeProgramFX(view).getFeatures(featureExtractors, featureResources);
    }

    public void printMethodToCSV(Set<MethodFeatureSet> set, String path) {
        boolean isFirst = true;
        try (OutputStream out = new FileOutputStream(path);
             Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            for (MethodFeatureSet featureSet : set) {
                String instanceName = featureSet.getMethod().getName();
                List<String> values = new ArrayList<>();
                if (isFirst) {
                    writer.append("name,");
                    for (Feature feature : featureSet.getFeatures()) {
                        if (isFirst) {
                            writer.append(feature.getName() + ",");
                            isFirst = false;
                        }
                    }
                    writer.append(System.lineSeparator());
                }
                writer.append(instanceName + ",");
                for (Feature feature : featureSet.getFeatures()) {
                    if (isFirst) {
                        writer.append(feature.getName() + ",");
                        isFirst = false;
                    }
                }
                writer.append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void printMultiSetToCSV(Set<? extends AbstractFeatureSet> set, String path) throws IOException {
        boolean isFirst = true;
        File file = new File(path);
        if(file.getParentFile()!=null){
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        try (OutputStream out = new FileOutputStream(file);
             Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            for (AbstractFeatureSet featureSet : set) {
                String instanceName = featureSet.getSignature();
                if (isFirst) {
                    writer.append("name;");
                    for (Feature feature : featureSet.getFeatures()) {
                        writer.append(feature.getName() + ";");
                    }
                    writer.append(System.lineSeparator());
                    isFirst = false;
                }
                writer.append(instanceName + ";");
                for (Feature feature : featureSet.getFeatures()) {
                    writer.append(feature.getValue().toString() + ";");
                }
                writer.append(System.lineSeparator());
            }
        }
    }

    public void printSingleSetToCSV(AbstractFeatureSet set, String path) throws IOException {
        boolean isFirst = true;
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (OutputStream out = new FileOutputStream(file);
             Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            String instanceName = set.getSignature();
            writer.append("name;");
            for (Feature feature : set.getFeatures()) {
                writer.append(feature.getName() + ";");
            }
            writer.append(System.lineSeparator());
            writer.append(instanceName + ";");
            for (Feature feature : set.getFeatures()) {
                writer.append(feature.getValue().toString() + ";");
            }
        }
    }

    private String getApkPath() {
        Optional<String> apkPath = classPaths.stream().filter(c -> c.endsWith(".apk")).findFirst();
        if (!apkPath.isPresent()) {
            throw new RuntimeException("classPaths do not contain any apk files");
        }
        return apkPath.get();
    }


}
