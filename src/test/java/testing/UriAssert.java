package testing;

import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.Assertions;

import java.net.URI;
import java.net.URISyntaxException;

public class UriAssert extends AbstractUriAssert<UriAssert> {

    protected UriAssert(URI actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public static UriAssert assertThat(URI actual) {
        return new UriAssert(actual, UriAssert.class);
    }

    public void isEqualToWithoutQueryParameters(String expected) {
        try {
            URI uri = new URI(actual.getScheme(),
                    actual.getAuthority(),
                    actual.getPath(),
                    null, // Ignore the query part of the input url
                    actual.getFragment());

            Assertions.assertThat(uri.toString()).isEqualTo(expected);

        } catch (URISyntaxException e) {
            throw failure("Failed to construct URI from provided");
        }
    }

    public void hasNotEmptyQueryParameter(String parameterName) {
        String value = UriUtils.parseQueryParameters(actual).get(parameterName);

        Assertions.assertThat(value).isNotEmpty();
    }
}
