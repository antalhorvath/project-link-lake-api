package com.vathevor.shared.spring.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vathevor.shared.util.ShortUUID;

import java.io.IOException;

public class ShortUUIDDeserializer extends JsonDeserializer<ShortUUID> {

    @Override
    public ShortUUID deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        return new ShortUUID(value);
    }
}
