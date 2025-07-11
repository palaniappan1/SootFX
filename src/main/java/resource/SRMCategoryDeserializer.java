package resource;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SRMCategoryDeserializer extends JsonDeserializer<SRMCategory> {

    @Override
    public SRMCategory deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        if (!node.isArray() || node.isEmpty()) {
            return null;
        }

        String firstValue = node.get(0).asText();
        return SRMCategory.fromString(firstValue);
    }
}

