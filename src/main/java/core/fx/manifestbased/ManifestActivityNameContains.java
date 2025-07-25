package core.fx.manifestbased;

import core.fx.base.Feature;
import core.fx.base.ManifestFEU;
import org.apache.commons.lang3.StringUtils;
import soot.jimple.infoflow.android.manifest.ProcessManifest;

public class ManifestActivityNameContains implements ManifestFEU<Boolean> {

    String value;

    public ManifestActivityNameContains(String value) {
        this.value = value;
    }

    @Override
    public Feature<Boolean> extract(ProcessManifest target) {
        boolean contains = false;
        String name = target.getActivities().asList().get(0).getNameString();
        return new Feature<>(this.getClass().getSimpleName(), StringUtils.containsIgnoreCase(name, value));
    }

}
