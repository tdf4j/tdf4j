@ParametersAreNonnullByDefault
@Value.Style(
        strictBuilder = true,
        passAnnotations = {Nullable.class, Nonnull.class}
)
package io.github.therealmone.tdf4j.parser.api;

import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;