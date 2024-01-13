package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.web.CelloOauth2ServerEndpointsSpec;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Redirect to consent page after successful resource owner authentication
 */
public class ConsentPageRedirectingResourceOwnerAuthenticationSuccessHandler implements ResourceOwnerAuthenticationSuccessHandler {
    private final URI consentPageUri;
    private final ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    public ConsentPageRedirectingResourceOwnerAuthenticationSuccessHandler(CelloOauth2ServerEndpointsSpec endpointsSpec) {
        consentPageUri = URI.create(endpointsSpec.getConsentEndpoint());
    }

    @Override
    @NotNull
    public Mono<Void> onAuthenticationSuccess(@NotNull ServerWebExchange exchange,
                                              @NotNull AuthenticatedResourceOwnerAuthentication authentication) {

        String flowId = exchange.getAttribute("flow_id");

        URI redirectLocation = UriComponentsBuilder.fromUri(consentPageUri)
                .queryParam("flow_id", flowId)
                .build()
                .toUri();

        return redirectStrategy.sendRedirect(exchange, redirectLocation);
    }
}
