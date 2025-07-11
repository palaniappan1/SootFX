package core.fx.wholeprogrambased;

public class WholeProgramSinkCount extends WholeProgramSourceSinkCount{

    @Override
    protected boolean isRelevantSignature(String signature) {
        return getSinkSignatures().contains(signature);
    }
}
