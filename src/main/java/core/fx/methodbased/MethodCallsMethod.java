package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.stmt.InvokableStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.Body;
import sootup.core.model.SootMethod;
import sootup.core.views.View;


import java.util.ArrayList;
import java.util.List;

public class MethodCallsMethod implements MethodFEU<Boolean> {

    private String className;
    private String methodName;
    @NonNull
    private View view;

    public MethodCallsMethod(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public MethodCallsMethod(View view) {
        this.view = view;
    }

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean calls = false;

        calls = checkMethod(target, new ArrayList<>());

        return new Feature<>(getName(className + "." + methodName), calls);
    }

    public boolean checkMethod(SootMethod method, List<SootMethod> doneList) {
        if (doneList.contains(method))
            return false;
        if (!method.isConcrete())
            return false;
        doneList.add(method);

        try {
            Body body = method.getBody();
            for (Stmt u : body.getStmts()) {
                if (!(u instanceof InvokableStmt && ((InvokableStmt) u).getInvokeExpr().isPresent()))
                    continue;

                AbstractInvokeExpr inv = ((InvokableStmt) u).getInvokeExpr().get();
                if (StringUtils.startsWithIgnoreCase(inv.getMethodSignature().getName(), this.methodName)) {
                    if (this.className.isEmpty() || this.className
                            .equals(inv.getMethodSignature().getDeclClassType().getClassName()))
                        return true;
                } else if (checkMethod(this.view.getMethod(inv.getMethodSignature()).get(), doneList))
                    return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
