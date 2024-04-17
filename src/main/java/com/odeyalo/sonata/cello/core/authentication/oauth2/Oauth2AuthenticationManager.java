package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Main class that handles a Oauth2 authentication using third-party service,
 *     <ul>
 *         <li> Starts an authentication by redirecting user to specific provider. </li>
 *         <li> Provider URI is resolved using {@link Oauth2ProviderRedirectUriGenerator} that generates a redirect URI for specific Oauth2 provider</li>
 *         <li> Then {@link Oauth2AuthenticationMetadata} is saved to {@link Oauth2AuthenticationMetadataRepository}. It is used to share a FLOW_ID between Oauth2 requests. </li>
 *     </ul>
 */
@Component
public final class Oauth2AuthenticationManager {

    private final Oauth2StateGenerator stateGenerator;
    private final Oauth2ProviderRedirectUriGenerator redirectUriGenerator;
    private final Oauth2AuthenticationMetadataRepository metadataRepository;
    private final ServerRedirectStrategy serverRedirectStrategy = new DefaultServerRedirectStrategy();

    public Oauth2AuthenticationManager(Oauth2StateGenerator stateGenerator,
                                       Oauth2ProviderRedirectUriGenerator redirectUriGenerator,
                                       Oauth2AuthenticationMetadataRepository metadataRepository) {
        this.stateGenerator = stateGenerator;
        this.redirectUriGenerator = redirectUriGenerator;
        this.metadataRepository = metadataRepository;
    }

    @NotNull
    public Mono<Void> startOauth2Authentication(@NotNull String providerName,
                                                @NotNull ServerWebExchange exchange) {
        return stateGenerator.generateState()
                .flatMap(state -> {
                    final String flowId = exchange.getAttribute(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME);

                    final Oauth2AuthenticationMetadata sharedMetadata = Oauth2AuthenticationMetadata.withFlowId(flowId);

                    final Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                            .withState(state)
                            .build();
                    return redirectUriGenerator.generateOauth2RedirectUri(providerName, context)
                            .flatMap(uri -> metadataRepository.save(state, sharedMetadata)
                                    .thenReturn(uri));
                })
                .flatMap(uri -> serverRedirectStrategy.sendRedirect(exchange, uri));
    }
}
