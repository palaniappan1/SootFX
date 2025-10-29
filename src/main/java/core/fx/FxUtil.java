package core.fx;

import api.FeatureDescription;
import api.FeatureGroup;
import api.SootFX;
import core.fx.base.ClassFEU;
import core.fx.base.MethodFEU;
import core.fx.base.WholeProgramFEU;
import org.reflections.Reflections;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.typehierarchy.TypeHierarchy;
import sootup.core.types.ClassType;
import sootup.core.types.Type;
import sootup.core.views.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FxUtil {

    private static Reflections reflections = new Reflections("core.fx");

    public static boolean isOfType(View view, Type type, String typeName) {
        if (!(type instanceof ClassType)) return false;

        // Check for a direct match
        ClassType refType = (ClassType) type;
        if (refType.getClass().getName().equals(typeName)) return true;

        // interface treatment
        if (view.getClass(refType).isPresent() && view.getClass(refType).get().isInterface()) return false;

        // class treatment
        TypeHierarchy h = view.getTypeHierarchy();

        Set<ClassType> classTypes = h.superClassesOf(refType).collect(Collectors.toSet());
        List<SootClass> ancestors = new ArrayList<>();
        classTypes.forEach(c -> view.getClass(c).ifPresent(ancestors::add));

        for (SootClass ancestor : ancestors) {
            if (ancestor.getName().equals(typeName)) return true;
            for (ClassType sc : ancestor.getInterfaces())
                if (sc.getClass().getName().equals(typeName)) return true;
        }
        return false;
    }

//    public static List<String> getManifestUsesFeature(ProcessManifest manifest) {
//        List<String> usesFeatures = new ArrayList<>();
//        List<AXmlNode> usesSdk = manifest.getManifest().getChildrenWithTag("uses-sdk");
//        if (usesSdk != null && !usesSdk.isEmpty()) {
//            for (AXmlNode aXmlNode : usesSdk) {
//                AXmlAttribute<?> nameAttr = aXmlNode.getAttribute("name");
//                if(nameAttr!=null){
//                    String name = (String) nameAttr.getValue();
//                    usesFeatures.add(name);
//                }
//            }
//        }
//        return usesFeatures;
//    }

    public static boolean isAppMethod(SootMethod m){
        String pkg = m.getDeclClassType().getPackageName().getName();
        if(SootFX.isAPK){
            return isAppPackage(pkg);
        }
        return isLibPackage(pkg);
    }

    public static boolean isAppClass(SootClass sc){
        String className = sc.getName();
        if(SootFX.isAPK){
            return isAppPackage(className);
        }
        return isLibPackage(className);
    }

    public static boolean isAppPackage(String name){
        return !name.startsWith("android.") && !name.startsWith("java.") && !name.startsWith("javax.") && !name.startsWith("dalvik.") && !name.startsWith("junit.") &&
                !name.startsWith("org.apache") && !name.startsWith("org.json") && !name.startsWith("org.w3c") && !name.startsWith("org.xml")
                && !name.startsWith("org.ietf.") && !name.startsWith("org.omg.") && !name.startsWith("sun.") && !name.startsWith("jdk.");
    }

    public static boolean isLibPackage(String name){
        return !name.startsWith("android.") && !name.startsWith("java.") && !name.startsWith("javax.") && !name.startsWith("dalvik.") && !name.startsWith("sun.") && !name.startsWith("jdk.");
    }

    public static List<FeatureDescription> listAllMethodFeatures(){
        List<FeatureDescription> methodList = new ArrayList<>();
        Set<Class<? extends MethodFEU>> methodBased = reflections.getSubTypesOf(MethodFEU.class);
        for (Class<? extends MethodFEU> m : methodBased) {
            FeatureDescription desc = new FeatureDescription(m.getSimpleName(), ""); // TODO: define descriptions and get
            methodList.add(desc);
        }
        return methodList;
    }

    public static List<FeatureDescription> listAllClassFeatures(){
        Set<Class<? extends ClassFEU>> classBased = reflections.getSubTypesOf(ClassFEU.class);
        List<FeatureDescription> classList = new ArrayList<>();
        for (Class<? extends ClassFEU> m : classBased) {
            FeatureDescription desc = new FeatureDescription(m.getSimpleName(), ""); // TODO: define descriptions and get
            classList.add(desc);
        }
        return classList;
    }

    public static List<FeatureDescription> listAllWholeProgramFeatures(){
        Set<Class<? extends WholeProgramFEU>> wholeProgramBased = reflections.getSubTypesOf(WholeProgramFEU.class);
        List<FeatureDescription> wpList = new ArrayList<>();
        for (Class<? extends WholeProgramFEU> m : wholeProgramBased) {
            FeatureDescription desc = new FeatureDescription(m.getSimpleName(), ""); // TODO: define descriptions and get
            wpList.add(desc);
        }
        return wpList;
    }

//    public static List<FeatureDescription> listAllManifestFeatures(){
//        Set<Class<? extends ManifestFEU>> manifestBased = reflections.getSubTypesOf(ManifestFEU.class);
//        List<FeatureDescription> manifestList = new ArrayList<>();
//        for (Class<? extends ManifestFEU> m : manifestBased) {
//            FeatureDescription desc = new FeatureDescription(m.getSimpleName(), ""); // TODO: define descriptions and get
//            manifestList.add(desc);
//        }
//        return manifestList;
//    }

    public static List<FeatureGroup> listAllFeatures(){
        List<FeatureGroup> allFeatures = new ArrayList<>();

        List<FeatureDescription> methodList = listAllMethodFeatures();
        FeatureGroup methodGroup = new FeatureGroup("methodbased", methodList);
        allFeatures.add(methodGroup);

        List<FeatureDescription> classList = listAllClassFeatures();
        FeatureGroup classGroup = new FeatureGroup("classbased", classList);
        allFeatures.add(classGroup);

        List<FeatureDescription> wpList = listAllWholeProgramFeatures();
        FeatureGroup wpGroup = new FeatureGroup("wholeprogrambased", wpList);
        allFeatures.add(wpGroup);

//        List<FeatureDescription> manifestList = listAllManifestFeatures();
//        FeatureGroup manifestGroup = new FeatureGroup("wholeprogrambased", manifestList);
//        allFeatures.add(manifestGroup);

        return allFeatures;
    }

}
