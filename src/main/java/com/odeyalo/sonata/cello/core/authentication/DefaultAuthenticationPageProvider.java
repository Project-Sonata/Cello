package com.odeyalo.sonata.cello.core.authentication;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DefaultAuthenticationPageProvider implements AuthenticationPageProvider {

    @Override
    @NotNull
    public Mono<ServerHttpResponse> getAuthenticationPage(@NotNull ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.TEXT_HTML);
        return response.writeWith(
                        Flux.just(getContent()).map(s -> response.bufferFactory().wrap(s.getBytes()))
                )
                .thenReturn(response);

    }

    private String getContent() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Cello Oauth2 Login</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            text-align: center;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        .login-container {\n" +
                "            max-width: 400px;\n" +
                "            margin: 100px auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        .login-container h2 {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "\n" +
                "        .login-form {\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "\n" +
                "        .form-group {\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "\n" +
                "        .form-group label {\n" +
                "            display: block;\n" +
                "            margin-bottom: 5px;\n" +
                "            color: #555;\n" +
                "        }\n" +
                "\n" +
                "        .form-group input {\n" +
                "            width: 100%;\n" +
                "            padding: 8px;\n" +
                "            box-sizing: border-box;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "\n" +
                "        .form-group button {\n" +
                "            background-color: #4caf50;\n" +
                "            color: #fff;\n" +
                "            padding: 10px;\n" +
                "            border: none;\n" +
                "            border-radius: 4px;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"login-container\">\n" +
                "    <h2>Login</h2>\n" +
                "    <form class=\"login-form\" action=\"/login\" method=\"post\">\n" +
                "        <div class=\"form-group\">\n" +
                "            <label for=\"username\">Username:</label>\n" +
                "            <input type=\"text\" id=\"username\" name=\"username\" required>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"form-group\">\n" +
                "            <label for=\"password\">Password:</label>\n" +
                "            <input type=\"password\" id=\"password\" name=\"password\" required>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"form-group\">\n" +
                "            <button type=\"submit\">Login</button>\n" +
                "        </div>\n" +
                "    </form>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
    }
}
