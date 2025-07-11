package core.fx.wholeprogrambased;

public class WholeProgramSourceCount extends WholeProgramSourceSinkCount{

    @Override
    protected boolean isRelevantSignature(String signature) {
        return getSourceSignatures().contains(signature);
    }
}
