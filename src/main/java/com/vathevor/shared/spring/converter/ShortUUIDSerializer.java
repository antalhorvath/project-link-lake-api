package com.vathevor.shared.spring.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vathevor.shared.util.ShortUUID;

import java.io.IOException;

public class ShortUUIDSerializer extends JsonSerializer<ShortUUID> {

    @Override
    public void serialize(ShortUUID shortUUID, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (shortUUID == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(shortUUID.value());
        }
    }
}
