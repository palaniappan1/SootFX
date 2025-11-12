package core.fx.methodbased;

import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;

public class MethodBranchCount implements MethodFEU<Long> {

  @Override
  public Feature<Long> extract(SootMethod method) {
    Long count = -1l;

    if (method.hasBody()) {
      count = method.getBody().getStmts().stream().filter(Stmt::branches).count();
    }
    return new Feature<>(getName(), count);
  }
}
