package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@Value
@Builder
public class ErrorMessage {
    @NotNull
    String error;
    @Nullable
    String description;
    @Nullable
    String errorUri;

    @NotNull
    public static ErrorMessage withErrorCode(@NotNull final Oauth2ErrorCode code) {
        return ErrorMessage.builder().error(code.asSpecificationString()).build();
    }


    /**
     * @return a ready to use map that can be serialized to JSON or XML,
     * following the Oauth2 rules
     */
    @NotNull
    public Map<String, Object> asMap() {
        return Map.of(
                "error", error,
                "error_description", Objects.requireNonNullElse(description, "null"),
                "error_uri", Objects.requireNonNullElse(errorUri, "null")
        );
    }
}
