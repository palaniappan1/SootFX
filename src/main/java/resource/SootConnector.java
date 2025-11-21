package resource;

import static api.SootFX.isAPK;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import sootup.apk.frontend.ApkAnalysisInputLocation;
import sootup.apk.frontend.DexBodyInterceptors;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.views.View;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.views.JavaView;

public class SootConnector {

  public static View sootupConnector(List<String> classPaths, String androidJars) {
    if (classPaths == null || classPaths.isEmpty()) {
      throw new IllegalArgumentException("classPaths must contain at least one path");
    }
    String cp = String.join(File.pathSeparator, classPaths);
    if (isAPK) {
      List<AnalysisInputLocation> apkAnalysisInputLocations = new ArrayList<>();
      classPaths.forEach(
          c -> {
            apkAnalysisInputLocations.add(
                new ApkAnalysisInputLocation(
                    Paths.get(c), androidJars, DexBodyInterceptors.Default.bodyInterceptors()));
          });
      return new JavaView(apkAnalysisInputLocations);
    } else {
      List<AnalysisInputLocation> analysisInputLocation = new ArrayList<>();

      analysisInputLocation.add(new JavaClassPathAnalysisInputLocation(cp));
      //            analysisInputLocation.add(new DefaultRuntimeAnalysisInputLocation());
      return new JavaView(analysisInputLocation);
    }
  }
}
