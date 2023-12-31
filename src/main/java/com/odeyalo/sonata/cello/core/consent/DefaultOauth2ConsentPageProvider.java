package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
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

        return response.writeWith(Flux.just(response.bufferFactory().wrap(("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>OAuth2 Consent Page</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: auto;\n" +
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
                "        <label>\n" +
                "            <input type=\"checkbox\" name=\"scope[]\" value=\"read_data\" required>\n" +
                "            Read Data\n" +
                "        </label>\n" +
                "\n" +
                "        <label>\n" +
                "            <input type=\"checkbox\" name=\"scope[]\" value=\"write_data\" required>\n" +
                "            Write Data\n" +
                "        </label>\n" +
                "\n" +
                "        <button type=\"submit\" class=\"permit\" name=\"action\" value=\"approved\">Permit</button>\n" +
                "        <button type=\"submit\" class=\"deny\" name=\"action\" value=\"denied\">Deny</button>\n" +
                "    </form>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n").getBytes())));


    }
}
