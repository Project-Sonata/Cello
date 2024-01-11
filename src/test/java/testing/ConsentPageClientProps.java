package testing;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.test.web.reactive.server.WebTestClient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsentPageClientProps {
    String flowId;
    CelloWebTestClient.AuthenticatedClientProps authenticatedClientProps;
    WebTestClient webTestClient;

    public static Builder builder(WebTestClient webTestClient) {
        return new Builder().withWebTestClient(webTestClient);
    }

    public static class Builder {
        private String flowId;
        private final CelloWebTestClient.AuthenticatedClientBuilder<Builder> authenticatedClientBuilder = new CelloWebTestClient.AuthenticatedClientBuilder<>(this);
        private WebTestClient webTestClient;

        public Builder withWebTestClient(WebTestClient webTestClient) {
            this.webTestClient = webTestClient;
            return this;
        }

        public CelloWebTestClient.AuthenticatedClientBuilder<Builder> authenticatedUser() {
            return authenticatedClientBuilder;
        }

        public Builder withFlowId(String flowId) {
            this.flowId = flowId;
            return this;
        }

        public ConsentPageClientProps build() {
            return new ConsentPageClientProps(flowId, authenticatedClientBuilder.build(), webTestClient);
        }

        public ConsentPageClient ready() {
            return new ConsentPageClient(webTestClient, this.build());
        }
    }
}
