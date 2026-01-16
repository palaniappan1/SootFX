package resource;

import org.apache.commons.lang3.StringUtils;
import soot.G;
import soot.Scene;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.CHATransformer;
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
        if(!StringUtils.isEmpty(androidJars)){
            Options.v().set_src_prec(Options.src_prec_apk);
            Options.v().set_process_multiple_dex(true);
            Options.v().set_android_jars(androidJars);
        }
        Options.v().set_whole_program(true);
        if (mainClass != null) {
            Options.v().set_main_class(mainClass);
        }
        Scene.v().loadNecessaryClasses();
        //SootMethod mainMethod = Scene.v().getMainMethod();
        //Scene.v().setEntryPoints(Collections.singletonList(mainMethod));

        HashMap opt = new HashMap();
        opt.put("enabled", "true");
        opt.put("vta","true");
        //opt.put("apponly","true");
        //SparkTransformer.v().transform("", opt);
//        CHATransformer.v().transform();
        InfoflowAndroidConfiguration infoflowAndroidConfiguration = new InfoflowAndroidConfiguration();
        infoflowAndroidConfiguration.setMergeDexFiles(true);
        infoflowAndroidConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        infoflowAndroidConfiguration.setDataFlowDirection(InfoflowConfiguration.DataFlowDirection.Forwards);
        infoflowAndroidConfiguration.getAnalysisFileConfig().setAndroidPlatformDir(new File(androidJars));
        infoflowAndroidConfiguration.getAnalysisFileConfig().setTargetAPKFile(new File(classPaths.get(0)));
        new SetupApplication(infoflowAndroidConfiguration, null).constructCallgraph();
    }
}
