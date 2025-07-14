package resource;

import application.CallGraphApplication;
import config.CallGraphAlgorithm;
import config.CallGraphConfig;
import metrics.CallGraphMetricsWrapper;
import org.apache.commons.lang3.StringUtils;
import soot.G;
import soot.Scene;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class SootConnector {

    public static void setupSoot(String mainClass, List<String> classPaths, boolean appOnly, String androidJars) {
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_include_all(true);
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_process_dir(classPaths);
        Options.v().set_keep_line_number(true);
        // set spark options for construct call graphs
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("cg.spark", "string-constants:true");
        Options.v().set_app(appOnly);
        if (!StringUtils.isEmpty(androidJars)) {
            Options.v().set_src_prec(Options.src_prec_apk);
            Options.v().set_keep_offset(false);
            Options.v().set_process_multiple_dex(true);
            Options.v().set_android_jars(androidJars);
//            Options.v().set_soot_classpath(androidJars + File.separatorChar + "android-" + 32 + File.separatorChar + "android.jar");
        } else {
            Options.v().set_soot_classpath(System.getProperty("java.home"));
            Options.v().set_src_prec(Options.src_prec_class);
        }
        Options.v().set_whole_program(true);
        if (mainClass != null) {
            Options.v().set_main_class(mainClass);
        }
        //SootMethod mainMethod = Scene.v().getMainMethod();
        //Scene.v().setEntryPoints(Collections.singletonList(mainMethod));

        HashMap opt = new HashMap();
        opt.put("enabled", "true");
        opt.put("vta", "true");
        //opt.put("apponly","true");
        //SparkTransformer.v().transform("", opt);
        // For an apk file, crating call graph is hard, so we utilize the QCG framework which in turn uses flowdroid to create the call graph
        if (!StringUtils.isEmpty(androidJars)) {
            CallGraphConfig instance = CallGraphConfig.getInstance();
            instance.setCallGraphAlgorithm(CallGraphAlgorithm.CHA);
            instance.setAppPath(classPaths.get(0));
            instance.setIsSootSceneProvided(false);
            CallGraphMetricsWrapper callGraphMetricsWrapper = CallGraphApplication.generateCallGraphFromPath(instance);
            Scene.v().setCallGraph(callGraphMetricsWrapper.getCallGraph());
        }
        else{
            // Load necessary classes is a time-consuming process, so it should happen only for the jar file. For an APK, the QCG framework takes care of it
            Scene.v().loadNecessaryClasses();
            CHATransformer.v().transform();
        }
    }
}
