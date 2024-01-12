package com.odeyalo.sonata.cello.spring.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.odeyalo.sonata.cello")
@ConditionalOnProperty(value = "cello.enabled", havingValue = "true")
public class CelloServerAutoConfiguration {

}
