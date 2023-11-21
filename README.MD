## Cello 
Cello is a Oauth2 Authorization Server written in Reactive manner using Spring Webflux.
Cello provides autoconfiguration out-of-the-box, you need only to configure some basic properties.


Cello Features:
- Easy to customize
- Lightweight

# Configuration

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCelloOauth2Server
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}
```

That's it! The Cello will configure endpoints, token generators and all required beans!

# Cello events
This is very important part of the Cello project. Events will be published every time, and it is very useful to handle them!
Events supported by cello:
- ConsentApprovedEvent
- ConsentDeniedEvent
- InvalidClientCredentialsEvent
- SuccessUserLoginEvent
- FailedUserLoginEvent
- UnknownRedirectUriReceivedEvent
- AuthorizationRequestReceivedEvent
# Consent page
Consent page is a important part of the Oauth2, and it is reasonable to be configured easy!