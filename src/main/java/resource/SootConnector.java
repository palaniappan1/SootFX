package resource;

import org.apache.commons.lang3.StringUtils;
import soot.G;
import soot.PackManager;
import soot.Scene;
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
        if (!StringUtils.isEmpty(androidJars)) {
            Options.v().set_src_prec(Options.src_prec_apk);
            Options.v().set_keep_offset(false);
            Options.v().set_process_multiple_dex(true);
            Options.v().set_android_jars(androidJars);
            Options.v().set_force_android_jar(androidJars+ File.separatorChar + "android-" + 32 + File.separatorChar + "android.jar" );
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
        Scene.v().loadNecessaryClasses();
        HashMap opt = new HashMap();
        opt.put("enabled", "true");
        opt.put("vta", "true");
        PackManager.v().getPack("wjpp").apply();
        //opt.put("apponly","true");
        //SparkTransformer.v().transform("", opt);
        CHATransformer.v().transform();
    }
}
