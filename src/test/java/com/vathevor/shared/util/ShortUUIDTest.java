package com.vathevor.shared.util;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ShortUUIDTest {

    Predicate<String> containsOnlyAlphaNumericLetters = value -> value.matches("[0-9a-fA-F]+");
    Predicate<String> containsThirtyTwoLetters = value -> value.length() == 32;
    Predicate<String> shortUuidStringFormat = containsOnlyAlphaNumericLetters.and(containsThirtyTwoLetters);

    @RepeatedTest(value = 100)
    void creates_simple_uuid_via_empty_constructor() {
        var uuid = new ShortUUID();
        assertThat(uuid.value())
                .matches(shortUuidStringFormat);
    }

    @RepeatedTest(value = 100)
    void generates_simple_uuid() {
        var uuid = ShortUUID.randomUUID();
        assertThat(uuid.value())
                .matches(shortUuidStringFormat);
    }

    @RepeatedTest(value = 100)
    void creates_simple_uuid_from_hyphenless_uuid_string() {
        var hyphenlessUuidString = UUID.randomUUID()
                .toString()
                .replace("-", "");

        assertThat(ShortUUID.fromString(hyphenlessUuidString).value())
                .matches(shortUuidStringFormat);
    }

    @RepeatedTest(value = 100)
    void creates_simple_uuid_from_standard_uuid() {
        var uuid = ShortUUID.fromUUID(UUID.randomUUID());
        assertThat(uuid.value())
                .matches(shortUuidStringFormat);
    }

    @RepeatedTest(value = 100)
    void creates_simple_uuid_from_standard_uuid_string() {
        var uuid = ShortUUID.fromUUIDString(UUID.randomUUID().toString());
        assertThat(uuid.value()).matches(shortUuidStringFormat);
    }

    @ParameterizedTest(name = "Throws exception when value \"{0}\" is not a hyphenless UUID.")
    @NullSource
    @ValueSource(strings = {
            "",
            "not a uuid",
            "fa4bc4e97fcb4f37a582d12858d5dcb", // 31 letters
            "fa4bc4e97fcb4f37a582d12858d5dcbec", // 33 letters
            "fa4bc4e97fcb4f37a582d12858d5d???" // contains invalid character
    })
    void throws_exception_when_value_is_not_a_hyphenless_uuid(String value) {
        assertThatThrownBy(() -> ShortUUID.fromUUIDString(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @RepeatedTest(value = 100)
    void converts_to_standard_uuid() {
        var uuid = new ShortUUID();
        assertDoesNotThrow(uuid::toUUID);
    }

    @RepeatedTest(value = 100)
    void stringifies_value_only() {
        assertThat(ShortUUID.randomUUID().toString())
                .matches(shortUuidStringFormat);
    }
}
