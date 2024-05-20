package com.vathevor.shared.util;

import java.util.Optional;
import java.util.UUID;

/**
 * Utility class to handle java.util.UUID value type without hyphens.
 * @param value ShortUUID value must be 32 characters long hyphenless UUID String
 */
public record ShortUUID(String value) {

    private static final String VALIDATION_MESSAGE = "ShortUUID value must be 32 characters long hyphenless UUID String";

    public ShortUUID {
        if(value.length() != 32) {
            throw new IllegalArgumentException(VALIDATION_MESSAGE);
        }
        try {
            toUUID(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(VALIDATION_MESSAGE);
        }
    }

    /**
     * Creates a ShortUUID with hyphenless String value of UUID.randomUUID()
     */
    public ShortUUID() {
        this(removeHyphens(UUID.randomUUID()));
    }

    /**
     * Alias for constructor.
     * Generates a ShortUUID with hyphenless String value of UUID.randomUUID().
     * @return ShortUUID
     */
    public static ShortUUID randomUUID() {
        return new ShortUUID();
    }

    private static String removeHyphens(UUID uuid) {
        return removeHyphens(uuid.toString());
    }

    public static ShortUUID fromString(String shortUUIDString) {
        return new ShortUUID(shortUUIDString);
    }

    /**
     * Creates a ShortUUID with hyphenless String value of the provided UUID.
     * @param uuid java.lang.UUID
     * @return ShortUUID
     */
    public static ShortUUID fromUUID(UUID uuid) {
        return fromUUIDString(uuid.toString());
    }

    /**
     * Creates a ShortUUID with hyphenless String value of the provided UUID.
     * @param uuidString standard UUID String
     * @return ShortUUID
     */
    public static ShortUUID fromUUIDString(String uuidString) {
        return Optional.ofNullable(uuidString)
                .map(ShortUUID::removeHyphens)
                .map(ShortUUID::new)
                .orElseThrow(() -> new IllegalArgumentException(VALIDATION_MESSAGE + ", cannot take null as value"));
    }

    private static String removeHyphens(String s) {
        return s.replace("-", "");
    }

    private static UUID toUUID(String value) {
        return UUID.fromString(
                value.substring(0, 8) + "-" +
                        value.substring(8, 12) + "-" +
                        value.substring(12, 16) + "-" +
                        value.substring(16, 20) + "-" +
                        value.substring(20, 32)
        );
    }

    /**
     * Converts the value to the standard java.util.UUID format.
     * @return UUID
     */
    public UUID toUUID() {
        return toUUID(this.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
