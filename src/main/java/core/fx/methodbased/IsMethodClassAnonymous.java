package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import sootup.core.model.SootMethod;

import java.util.regex.Pattern;

public class IsMethodClassAnonymous implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean isAnonymous = false;
        int index = target.getDeclaringClassType().getClassName().lastIndexOf("$");
        if(index != -1){
            try {
                String subclassName = target.getName().substring(index + 1);
                isAnonymous = Pattern.matches("^\\d+$", subclassName);
            }catch (Exception e){
                isAnonymous = false;
            }
        }
        return new Feature<>(getName(), isAnonymous);
    }
}
