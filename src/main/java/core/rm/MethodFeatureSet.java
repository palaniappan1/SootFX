package core.rm;

import java.util.TreeSet;
import sootup.core.model.SootMethod;

public class MethodFeatureSet extends AbstractFeatureSet {

  private SootMethod sootMethod;

  public MethodFeatureSet(SootMethod sootMethod) {
    this.sootMethod = sootMethod;
    this.features = new TreeSet<>();
  }

  @Override
  public String getSignature() {
    return sootMethod.getSignature().toString();
  }

  public SootMethod getMethod() {
    return sootMethod;
  }
}
