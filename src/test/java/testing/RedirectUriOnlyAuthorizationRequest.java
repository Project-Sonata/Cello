package testing;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2ResponseType;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
public class RedirectUriOnlyAuthorizationRequest implements Oauth2AuthorizationRequest {
    RedirectUri redirectUri;

    @Override
    public @NotNull String getClientId() {
        return "123";
    }

    @Override
    public @NotNull ScopeContainer getScopes() {
        return ScopeContainer.empty();
    }

    @Override
    public @Nullable String getState() {
        return null;
    }

    @Override
    public @NotNull Oauth2ResponseType getResponseType() {
        return Oauth2ResponseType.UNKNOWN;
    }
}
