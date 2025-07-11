package eval;

import api.SootFX;
import com.google.common.base.Stopwatch;
import core.fx.base.MethodFEU;
import core.fx.base.WholeProgramFEU;
import core.fx.wholeprogrambased.WholeProgramSinkCount;
import core.fx.wholeprogrambased.WholeProgramSourceCount;
import core.fx.wholeprogrambased.WholeProgramSourceSinkCount;
import core.rm.ClassFeatureSet;
import core.rm.MethodFeatureSet;
import core.rm.WholeProgramFeatureSet;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EvaluationJar {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String path = "/Users/palaniappanmuthuraman/WorkSpace/LinearConstantPropagation/IDELinearConstantAnalysisClientSootUp/src/test/resources/latest/commons-codec-1.15.jar";
        String out = "/Users/palaniappanmuthuraman/WorkSpace/SootFX/whole_output.csv";
//        executeAllFeatures(path, out);
        HashSet<WholeProgramFEU> wholeProgramFEUS = new HashSet<>();
        wholeProgramFEUS.add(new WholeProgramSourceCount());
        wholeProgramFEUS.add(new WholeProgramSinkCount());
        wpFeaturesInclude(path, out, wholeProgramFEUS);
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
    }

    public static void executeAllFeatures(String path, String out){
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();

            methodFeatures(path, out);
            long methodDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            classFeatures(path, out);
            long classDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            wpFeatures(path, out);
            long wpDone = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            logMeta(out, methodDone, classDone - methodDone, wpDone - classDone, new File(path).length());
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("error in jarFile:");
        }
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

    public static void classFeatures(String path, String out) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        Set<ClassFeatureSet> featureSets = sootFX.extractAllClassFeatures();
        sootFX.printMultiSetToCSV(featureSets, out + "class.csv");
    }

    public static void methodFeatures(String path, String out) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        Set<MethodFeatureSet> featureSets = sootFX.extractAllMethodFeatures();
        sootFX.printMultiSetToCSV(featureSets, out + "method.csv");
    }

    public static void wpFeaturesInclude(String path, String out, Set<WholeProgramFEU> methodFEUSet) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        WholeProgramFeatureSet wholeProgramFeatureSet = sootFX.extractWholeProgramFeaturesInclude(methodFEUSet);
        sootFX.printSingleSetToCSV(wholeProgramFeatureSet, out + "wp_include.csv");
    }

    public static void wpFeatures(String path, String out) throws IOException {
        SootFX sootFX = new SootFX();
        sootFX.addClassPath(path);
        sootFX.appOnly();
        WholeProgramFeatureSet featureSet = sootFX.extractAllWholeProgramFeatures();
        sootFX.printSingleSetToCSV(featureSet, out + "wp.csv");
    }


}
