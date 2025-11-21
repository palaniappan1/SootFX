package core.rm;

import java.util.TreeSet;
import sootup.core.model.SootClass;

public class ClassFeatureSet extends AbstractFeatureSet {

  private SootClass sootClass;

  public ClassFeatureSet(SootClass sootClass) {
    this.sootClass = sootClass;
    this.features = new TreeSet<>();
  }

  @Override
  public String getSignature() {
    return sootClass.getName();
  }
}
