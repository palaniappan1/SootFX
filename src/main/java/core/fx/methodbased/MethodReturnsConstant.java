package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import sootup.core.jimple.common.constant.Constant;
import sootup.core.jimple.common.stmt.JReturnStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.Body;
import sootup.core.model.SootMethod;

public class MethodReturnsConstant implements MethodFEU<Boolean> {

    @Override
    public Feature<Boolean> extract(SootMethod target) {
        boolean constant = false;

        if (target.hasBody()) {
            Body body = target.getBody();
            for(Stmt u: body.getStmts()){
                if(u instanceof JReturnStmt){
                    JReturnStmt ret = (JReturnStmt) u;
                    if(ret.getOp() instanceof Constant){
                        constant = true;
                        break;
                    }
                }
            }
        }
        return new Feature<>(getName(), constant);
    }
}
