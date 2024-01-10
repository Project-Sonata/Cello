package testing;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Creates the {@link AuthenticatedResourceOwnerAuthentication} from provided values
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthenticatedResourceOwnerSecurityContextFactory.class)
public @interface WithAuthenticatedResourceOwner {

    String username() default "user";

    String password() default "";

    String[] authorities() default {};

}
