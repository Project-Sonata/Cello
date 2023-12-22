package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;

public record Oauth2AuthorizationExchange(@NotNull AuthorizationRequest request,
                                          @NotNull Oauth2AuthorizationResponse response) {
}
