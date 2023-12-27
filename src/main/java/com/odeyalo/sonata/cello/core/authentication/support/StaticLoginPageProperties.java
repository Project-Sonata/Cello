package com.odeyalo.sonata.cello.core.authentication.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class StaticLoginPageProperties {
    /**
     * Path to html page that should be returned to user
     */
    @NotNull
    String path;
}
