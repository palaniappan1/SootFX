package core.fx.classbased;

import core.fx.base.ClassFEU;
import core.fx.base.Feature;
import soot.SootClass;
import soot.SootMethod;
import soot.util.Chain;

import java.util.HashSet;
import java.util.Set;

public class isPolymorphicClass implements ClassFEU<Boolean> {
    @Override
    public Feature<Boolean> extract(SootClass target) {
        SootClass superClass = target.getSuperclass();
        Set<String> superMethods = new HashSet<>();
        if (superClass != null) {
            superClass.getMethods().forEach(sm -> superMethods.add(sm.getSubSignature()));
        }

        // Get interface methods
        Set<String> interfaceMethods = new HashSet<>();
        Chain<SootClass> interfaces = target.getInterfaces();
        for (SootClass iface : interfaces) {
            iface.getMethods().forEach(im -> interfaceMethods.add(im.getSubSignature()));
        }

        // Check if any method overrides a superclass or interface method
        for (SootMethod m : target.getMethods()) {
            String sig = m.getSubSignature();
            if (superMethods.contains(sig) || interfaceMethods.contains(sig)) {
                // Class is polymorphic
                return new Feature<>(getName(), true);
            }
        }

        return new Feature<>(getName(),false); // no overriding found
    }
}
