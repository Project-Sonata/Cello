package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.OpenIdOauth2AccessTokenResponse;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.IdTokenAuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

/**
 * @deprecated Deprecated for a while
 */
@Component
@Deprecated
public final class IdTokenOauth2AccessTokenResponseAuthenticationConverter implements Oauth2AccessTokenResponseAuthenticationConverter {
    private final ObjectMapper objectMapper;

    public IdTokenOauth2AccessTokenResponseAuthenticationConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @NotNull
    public Mono<AuthenticatedResourceOwnerAuthentication> convertToAuthentication(@NotNull final Oauth2AccessTokenResponse token) {

        if ( !(token instanceof OpenIdOauth2AccessTokenResponse idTokenAware) ) {
            return Mono.empty();
        }

        String body = StringUtils.split(idTokenAware.getIdToken(), ".")[1];
        String jsonBody = new String(Base64.getDecoder().decode(body));

        Map<String, Object> map = readBody(jsonBody);

        String email = (String) map.get("name");

        System.out.println(map);

        return Mono.just(
                IdTokenAuthenticatedResourceOwnerAuthentication.create(email,  ResourceOwner.withPrincipalOnly(email), idTokenAware.getIdToken()));
    }

    private Map<String, Object> readBody(final String jsonBody) {
        try {
            return objectMapper.readValue(jsonBody, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
