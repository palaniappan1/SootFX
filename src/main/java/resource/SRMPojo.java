package resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SRMPojo {
    public String name;
    public List<String> parameters;
    public String signature;
    @JsonProperty("srm")
    @JsonDeserialize(using = SRMCategoryDeserializer.class)
    public SRMCategory srmCategories;
}