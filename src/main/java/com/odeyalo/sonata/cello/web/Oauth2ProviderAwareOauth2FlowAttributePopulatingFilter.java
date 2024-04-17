package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2AuthenticationMetadata;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2AuthenticationMetadataRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME;

/**
 * Extension for {@link Oauth2FlowAttributePopulatingFilter} that adds support for Oauth2
 */
public final class Oauth2ProviderAwareOauth2FlowAttributePopulatingFilter extends Oauth2FlowAttributePopulatingFilter {
    private final Oauth2AuthenticationMetadataRepository metadataRepository;
    private final ServerWebExchangeMatcher requestMatcher;

    public Oauth2ProviderAwareOauth2FlowAttributePopulatingFilter(CelloOauth2ServerEndpointsSpec endpointsSpec,
                                                                  Oauth2AuthenticationMetadataRepository metadataRepository, ServerWebExchangeMatcher requestMatcher) {
        super(endpointsSpec);
        this.metadataRepository = metadataRepository;
        this.requestMatcher = requestMatcher;
    }

    @Override
    @NotNull
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {

        return requestMatcher.matches(exchange)
                .flatMap(result -> {
                    if ( !result.isMatch() ) {
                        return super.filter(exchange, chain);
                    }

                    String state = exchange.getRequest().getQueryParams().getFirst("state");

                    if ( state == null ) {
                        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                        return exchange.getResponse().setComplete();
                    }

                    return metadataRepository.findBy(state)
                            .map(Oauth2AuthenticationMetadata::getFlowId)
                            .doOnNext(flowId -> exchange.getAttributes().put(CURRENT_FLOW_ATTRIBUTE_NAME, flowId))
                            .then(chain.filter(exchange));
                });

    }
}
