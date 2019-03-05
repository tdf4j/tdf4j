package io.github.therealmone.tdf4j.commons.bean;

import org.immutables.value.Value;

import java.util.regex.Pattern;

@Value.Immutable
public interface Terminal {
    String tag();

    Pattern pattern();

    @Value.Default
    default int priority() {
        return 0;
    }

    class Builder extends ImmutableTerminal.Builder {
        public Terminal.Builder pattern(final String pattern) {
            super.pattern(Pattern.compile(pattern));
            return this;
        }

        public Terminal.Builder pattern(final String pattern, final int flags) {
            super.pattern(Pattern.compile(pattern, flags));
            return this;
        }
    }
}
