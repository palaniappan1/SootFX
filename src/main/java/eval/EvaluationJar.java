package eval;

import api.Config;
import api.FeatureResource;
import com.google.common.base.Stopwatch;
import core.rm.ClassFeatureSet;
import core.rm.MethodFeatureSet;
import core.rm.WholeProgramFeatureSet;
import api.SootFX;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static api.CLI.getConfig;

public class EvaluationJar {

    public static void main(String[] args) throws IOException {
//        for (int i = 74; i <= 74; i++) {
//        String jarName = "mvn-" + i;
        String path =
//                "/Users/ashikmr/Desktop/contrastive_explanations/target/contrastive-explanations-0.3-SNAPSHOT-jar-with-dependencies.jar";

                "/Users/ashikmr/Desktop/soot-testing/test1/demo1.jar";
        String out = "/Users/ashikmr/Desktop/soot-testing/sootup/";

        String configPath = "/Users/ashikmr/Desktop/SootFX/config.yaml";
        Config config = getConfig(configPath);
        List<FeatureResource> featureResources = config.getFeatureResources();
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();

            methodFeatures(path, out, featureResources);
            long methodDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            classFeatures(path, out, featureResources);
            long classDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            wpFeatures(path, out, featureResources);
            long wpDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            logMeta(out, methodDone, classDone - methodDone, wpDone - classDone, new File(path).length());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("error in apk:");
        }
//        }
    }

    public static void logMeta(String path, long methodTime, long classTime, long wpTime, long fileSizeInBytes) throws IOException {
        path += "meta.csv";
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (OutputStream out = new FileOutputStream(file);
             Writer writer = new OutputStreamWriter(out, "UTF-8")) {
            writer.append("name;").append("value").append(System.lineSeparator());
            writer.append("method;").append(String.valueOf(methodTime)).append(System.lineSeparator());
            writer.append("class;").append(String.valueOf(classTime)).append(System.lineSeparator());
            writer.append("whole-program;").append(String.valueOf(wpTime)).append(System.lineSeparator());
            writer.append("size;").append(String.valueOf(fileSizeInBytes));
        }
    }

    public static void classFeatures(String path, String out, List<FeatureResource> featureResources) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        Set<ClassFeatureSet> featureSets = sootFX.extractAllClassFeatures(featureResources);
        sootFX.printMultiSetToCSV(featureSets, out + "classup.csv");
    }

    public static void methodFeatures(String path, String out, List<FeatureResource> featureResources) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        Set<MethodFeatureSet> featureSets = sootFX.extractAllMethodFeatures(featureResources);
        sootFX.printMultiSetToCSV(featureSets, out + "methodup.csv");
    }

    public static void wpFeatures(String path, String out, List<FeatureResource> featureResources) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        WholeProgramFeatureSet featureSet = sootFX.extractAllWholeProgramFeatures(featureResources);
        sootFX.printSingleSetToCSV(featureSet, out + "wpup.csv");
    }


}
