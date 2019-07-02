package io.github.therealmone.tdf4j.model;

import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(value = RetentionPolicy.CLASS)
@Value.Style(
        get = {"is*", "get*"},
        init = "set*",
        defaults = @Value.Immutable(copy = false),
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        strictBuilder = true,
        passAnnotations = {Nullable.class, Nonnull.class}
)
public @interface Tdf4jStyle {}
