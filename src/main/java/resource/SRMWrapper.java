package resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SRMWrapper {
    public String version;
    public List<SRMPojo> methods;
}

