package com.odeyalo.sonata.cello.core.authentication.oauth2;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Oauth2AuthenticationMetadata {
    @Getter(value = AccessLevel.PRIVATE)
    @Singular(value = "item")
    Map<String, Object> cache;

    private static final String FLOW_ID_KEY = "flow_id";

    public static Oauth2AuthenticationMetadata withFlowId(@NotNull String flowId) {
        return Oauth2AuthenticationMetadata.builder()
                .item(FLOW_ID_KEY, flowId)
                .build();
    }

    public int size() {
        return getCache().size();
    }

    public boolean isEmpty() {
        return getCache().isEmpty();
    }

    public boolean containsKey(Object key) {
        return getCache().containsKey(key);
    }

    public Object get(Object key) {
        return getCache().get(key);
    }

    @NotNull
    public String getFlowId() {
        return (String) cache.get(FLOW_ID_KEY);
    }

    public void putFlowId(@NotNull String flowId) {
        cache.put(FLOW_ID_KEY, flowId);
    }
}
