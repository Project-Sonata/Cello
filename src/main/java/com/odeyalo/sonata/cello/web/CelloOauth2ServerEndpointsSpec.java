package com.odeyalo.sonata.cello.web;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Contain specification of names for endpoints that used by Cello
 */
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public final class CelloOauth2ServerEndpointsSpec {
    @NotNull
    @Builder.Default
    String prefix = "/oauth2";
    @NotNull
    @Builder.Default
    String authorizeEndpoint = "/authorize";
    @NotNull
    @Builder.Default
    String loginEndpoint = "/login";
    @NotNull
    @Builder.Default
    String consentEndpoint = "/consent";


    private static final String SLASH = "/";

    public static CelloOauth2ServerEndpointsSpec defaultSpec() {
        return builder()
                .prefix("/oauth2")
                .consentEndpoint("/consent")
                .loginEndpoint("/login")
                .authorizeEndpoint("/authorize")
                .build();
    }

    public CelloOauth2ServerEndpointsSpec(@NotNull String prefix, @NotNull String authorizeEndpoint, @NotNull String loginEndpoint, @NotNull String consentEndpoint) {
        setPrefix(prefix);
        setAuthorizeEndpoint(authorizeEndpoint);
        setLoginEndpoint(loginEndpoint);
        setConsentEndpoint(consentEndpoint);
    }

    private void setPrefix(@NotNull String prefix) {
        if ( !StringUtils.startsWith(prefix, SLASH) ) {
            prefix = StringUtils.join(SLASH, prefix);
        }
        if ( !StringUtils.endsWith(prefix, SLASH) ) {
            prefix = StringUtils.join(prefix, SLASH);
        }
        this.prefix = prefix;
    }

    private void setAuthorizeEndpoint(@NotNull String authorizeEndpoint) {
        this.authorizeEndpoint = StringUtils.join(
                prefix,
                maybeStripSlash(authorizeEndpoint)
        );
    }


    private void setLoginEndpoint(@NotNull String loginEndpoint) {
        this.loginEndpoint = StringUtils.join(
                prefix,
                maybeStripSlash(loginEndpoint)
        );
    }

    private void setConsentEndpoint(@NotNull String consentEndpoint) {
        this.consentEndpoint = StringUtils.join(
                prefix,
                maybeStripSlash(consentEndpoint)
        );
    }


    @NotNull
    private static String maybeStripSlash(@NotNull String uri) {
        if ( StringUtils.startsWith(uri, SLASH) ) {
            return uri.substring(1);
        }
        return uri;
    }
}
