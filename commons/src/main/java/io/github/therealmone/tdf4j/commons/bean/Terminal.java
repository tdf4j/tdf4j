package io.github.therealmone.tdf4j.commons.bean;

import org.immutables.value.Value;

import java.util.regex.Pattern;

@Value.Immutable
public interface Terminal {
    String tag();

    Pattern pattern();

    int priority();
}
