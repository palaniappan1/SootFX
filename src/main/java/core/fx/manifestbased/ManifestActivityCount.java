package core.fx.manifestbased;

import core.fx.base.Feature;
import core.fx.base.ManifestFEU;
import soot.jimple.infoflow.android.manifest.ProcessManifest;

public class ManifestActivityCount implements ManifestFEU<Long> {

    @Override
    public Feature<Long> extract(ProcessManifest target) {
        Long count = (long) target.getActivities().asList().size();
        return new Feature<>(this.getClass().getSimpleName(), count);
    }

}
