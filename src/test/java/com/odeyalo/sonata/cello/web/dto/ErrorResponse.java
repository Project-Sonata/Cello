package com.odeyalo.sonata.cello.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class ErrorResponse {
    @JsonProperty
    String error;
    @JsonProperty("error_description")
    String description;
    @JsonProperty("error_uri")
    String errorUri;
}
