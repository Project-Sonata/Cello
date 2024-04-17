package com.odeyalo.sonata.cello.core.authentication.oauth2;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class Oauth2AuthenticationRedirectUriGenerationContext {
    @NotNull
    String state;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String state;

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Oauth2AuthenticationRedirectUriGenerationContext build() {
            return new Oauth2AuthenticationRedirectUriGenerationContext(state);
        }
    }

}
