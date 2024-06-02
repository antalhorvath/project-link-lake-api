package com.vathevor.shared.spring.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShortUUIDSerializerTest {

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule()
                .addDeserializer(ShortUUID.class, new ShortUUIDDeserializer())
                .addSerializer(ShortUUID.class, new ShortUUIDSerializer()));
    }

    record DummyDto(ShortUUID id) {
    }

    @Nested
    class Serializer {

        @Test
        void serializes_ShortUUID_as_string() throws JsonProcessingException {
            DummyDto dummyDto = new DummyDto(ShortUUID.fromString("1cc31b8fb35740249a6e183267d3e66d"));
            String serialisedDto = objectMapper.writeValueAsString(dummyDto);
            assertThat(serialisedDto).isEqualTo("{\"id\":\"1cc31b8fb35740249a6e183267d3e66d\"}");
        }

        @Test
        void serializes_null_ShortUUID_as_null() throws JsonProcessingException {
            DummyDto dummyDto = new DummyDto(null);
            String serialisedDto = objectMapper.writeValueAsString(dummyDto);
            assertThat(serialisedDto).isEqualTo("{\"id\":null}");
        }
    }

    @Nested
    class DeSerializer {

        @Test
        void de_serializes_string_as_ShortUUID() throws JsonProcessingException {
            DummyDto dummyDto = new DummyDto(ShortUUID.fromString("1cc31b8fb35740249a6e183267d3e66d"));
            DummyDto deSerialisedDto = objectMapper.readValue("{\"id\":\"1cc31b8fb35740249a6e183267d3e66d\"}", DummyDto.class);
            assertThat(deSerialisedDto).isEqualTo(dummyDto);
        }

        @Test
        void de_serializes_null_as_null_ShortUUID() throws JsonProcessingException {
            DummyDto dummyDto = new DummyDto(null);
            DummyDto deSerialisedDto = objectMapper.readValue("{\"id\":null}", DummyDto.class);
            assertThat(deSerialisedDto).isEqualTo(dummyDto);
        }
    }
}
