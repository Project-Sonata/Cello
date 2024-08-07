package testing.spring.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RegisteredClientRegistratorAutoConfiguration.class)
public @interface RegisterOauth2Clients {
    String DEFAULT_USERNAME = "123";
    String DEFAULT_PASSWORD = "password";

    String DEFAULT_CLIENT_2_USERNAME = "1234";
    String DEFAULT_CLIENT_2_PASSWORD = "miku-i-love-you";
}
