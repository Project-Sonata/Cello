package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.Scope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation that returns hardcoded consent page
 */
@Component
public final class DefaultOauth2ConsentPageProvider implements Oauth2ConsentPageProvider {

    @Override
    @NotNull
    public Mono<Void> getConsentPage(@NotNull Oauth2AuthorizationRequest request, @NotNull ResourceOwner resourceOwner, @NotNull ServerWebExchange httpExchange) {
        ServerHttpResponse response = httpExchange.getResponse();
        response.getHeaders().setContentType(MediaType.TEXT_HTML);
        String flowId = httpExchange.getAttribute(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME);

        return response.writeWith(
                Flux.just(response.bufferFactory().wrap(getContent(request, resourceOwner, flowId)))
        );
    }

    private static byte[] getContent(Oauth2AuthorizationRequest request, @NotNull ResourceOwner resourceOwner, String flowId) {
        ImplicitOauth2AuthorizationRequest implicitOauth2AuthorizationRequest = (ImplicitOauth2AuthorizationRequest) request;

        StringBuilder htmlContent = new StringBuilder("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>OAuth2 Consent Page</title>\n" +
                "    <style>\n" +
                "    body {\n" +
                "           font-family: Arial, sans-serif;\n" +
                "           display: flex;\n" +
                "           justify-content: center;\n" +
                "           align-items: center;\n" +
                "           height: 100vh;\n" +
                "           margin: 0;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #ccc;\n" +
                "        }\n" +
                "\n" +
                "        h2 {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            line-height: 1.6;\n" +
                "            color: #666;\n" +
                "        }\n" +
                "\n" +
                "        label {\n" +
                "            display: block;\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "\n" +
                "        button {\n" +
                "            display: inline-block;\n" +
                "            margin-top: 10px;\n" +
                "            padding: 10px 15px;\n" +
                "            border: none;\n" +
                "            border-radius: 5px;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "\n" +
                "        .permit {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "        }\n" +
                "\n" +
                "        .deny {\n" +
                "            background-color: #FF5733;\n" +
                "            color: white;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"container\">\n" +
                "    <h2>OAuth2 Consent Page</h2>\n" +
                "    <p>You are granting access to the following scopes:</p>\n" +
                "\n" +
                "    <form id=\"oauth2ConsentForm\" action=\"/oauth2/consent\" method=\"post\">\n" +
                "<input type=\"hidden\" name=\"flow_id\" value=\"" + flowId + "\">");

        for (Scope scope : implicitOauth2AuthorizationRequest.getScopes()) {
            htmlContent.append("<label>\n")
                    .append("<input type=\"checkbox\" name=\"approved_scope\" value=\"")
                    .append(scope.getName())
                    .append("\">")
                    .append(scope.getName())
                    .append("</label>\n");
        }

        htmlContent.append(
                        """
                                        <button type="submit" class="permit" name="action" value="approved">Permit</button>
                                        <button type="submit" class="deny" name="action" value="denied">Deny</button>
                                    </form>
                                """)
                .append("""
                         <div class="account">
                         <label>You are logged in as </label>
                        """
                )
                .append(resourceOwner.getPrincipal())
                .append("</div>")
                .append("</div>")
                .append(
                        """
                                               </body>
                                               </html>
                                """
                );

        return htmlContent.toString().getBytes();
    }
}
