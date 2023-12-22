package com.odeyalo.sonata.cello;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.InMemoryResourceOwnerService;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(CelloApplication.class, args);
    }

    @Bean
    public InMemoryResourceOwnerService resourceOwnerService() {
        return new InMemoryResourceOwnerService(List.of(
                ResourceOwner.builder()
                        .principal("odeyalo")
                        .credentials("123")
                        .availableScopes(ScopeContainer.singleScope(
                                SimpleScope.withName("write")
                        ))
                        .build()
        ));
    }
}
