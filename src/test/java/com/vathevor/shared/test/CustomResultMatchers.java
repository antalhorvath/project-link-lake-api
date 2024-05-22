package com.vathevor.shared.test;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class CustomResultMatchers {

    public static ResultMatcher statusIsNot(HttpStatus... status) {
        return result -> {
            for (HttpStatus httpStatus : status) {
                String message = "Status is unexpected: " + httpStatus.toString();
                assertTrue(message, +result.getResponse().getStatus() != httpStatus.value());
            }
        };
    }

    public static ResultMatcher statusIsEither(HttpStatus... status) {
        return result -> {

            boolean isMatch = Arrays.stream(status)
                    .anyMatch(s -> result.getResponse().getStatus() == s.value());

            String expectedStatusNames = Arrays.stream(status)
                    .map(HttpStatus::name)
                    .collect(Collectors.joining(",", "[", "]"));

            String message = "Status is incorrect: " +
                    Objects.requireNonNull(HttpStatus.resolve(result.getResponse().getStatus())).name() +
                    ". Expected status to be one of the followings: " + expectedStatusNames;

            assertTrue(message, isMatch);
        };
    }
}
