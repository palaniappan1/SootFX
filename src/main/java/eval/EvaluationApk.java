package eval;

import api.SootFX;
import com.google.common.base.Stopwatch;
import core.rm.*;

import java.io.*;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EvaluationApk {

    private static String androidPlatforms = "/Users/palaniappanmuthuraman/WorkSpace/Archived/Evaluation/Evaluation_TaintBench/supporting_files/platforms";

    public static void main(String[] args) {
            String path = "/Users/palaniappanmuthuraman/WorkSpace/Archived/Evaluation/Evaluation_TaintBench/apks/playstore_apks/excel.apk";
            String out = "/Users/palaniappanmuthuraman/WorkSpace/SootFX/whole_output.csv";

            try {
                Stopwatch stopwatch = Stopwatch.createStarted();

                methodFeatures(path, out);
                long methodDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

                classFeatures(path, out);
                long classDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

                wpFeatures(path, out);
                long wpDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

                manifestFeatures(path, out);
                long manifestDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

                logMeta(out, methodDone, classDone - methodDone, wpDone - classDone, manifestDone - wpDone, new File(path).length());
            } catch (Exception e){
                e.printStackTrace();
                System.err.println("error in apk");
            }
//        }
    }

    public static void logMeta(String path, long methodTime, long classTime, long wpTime, long manifestTime, long fileSizeInBytes) throws IOException {
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
            writer.append("manifest;").append(String.valueOf(manifestTime)).append(System.lineSeparator());
            writer.append("size;").append(String.valueOf(fileSizeInBytes));
        }
    }

    public static void classFeatures(String path, String out) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        sootFX.androidJars(androidPlatforms);
        Set<ClassFeatureSet> featureSets = sootFX.extractAllClassFeatures();
        sootFX.printMultiSetToCSV(featureSets, out + "class.csv");
    }

    public static void methodFeatures(String path, String out) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        sootFX.androidJars(androidPlatforms);
        Set<MethodFeatureSet> featureSets = sootFX.extractAllMethodFeatures();
        sootFX.printMultiSetToCSV(featureSets, out + "method.csv");
    }

    public static void wpFeatures(String path, String out) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        sootFX.androidJars(androidPlatforms);
        WholeProgramFeatureSet featureSet = sootFX.extractAllWholeProgramFeatures();
        sootFX.printSingleSetToCSV(featureSet, out + "wp.csv");
    }

    public static void manifestFeatures(String path, String out) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        sootFX.androidJars(androidPlatforms);
        ManifestFeatureSet featureSet = sootFX.extractAllManifestFeatures();
        sootFX.printSingleSetToCSV(featureSet, out + "manifest.csv");
    }

}
