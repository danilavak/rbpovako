package ru.vako.rbpovako;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class JsonTestHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonTestHelper() {
    }

    static Long idFrom(String json) throws Exception {
        JsonNode node = OBJECT_MAPPER.readTree(json);
        return node.get("id").asLong();
    }
}
