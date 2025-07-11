package resource;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SourceSinkIndex implements Serializable {
    public Set<String> sources = new HashSet<>();
    public Set<String> sinks = new HashSet<>();
}
