package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

import java.util.regex.Pattern;

@Value.Immutable
public abstract class Terminal implements Element {
    @Override
    public Kind kind() {
        return Kind.TERMINAL;
    }

    public abstract Tag tag();

    public abstract Pattern pattern();

    @Value.Default
    public long priority() {
        return 0;
    }

    public static class Builder extends ImmutableTerminal.Builder {
        public Terminal.Builder pattern(final String pattern) {
            super.pattern(Pattern.compile(pattern));
            return this;
        }

        public Terminal.Builder pattern(final String pattern, final int flags) {
            super.pattern(Pattern.compile(pattern, flags));
            return this;
        }

        public Terminal.Builder tag(final String tag) {
            super.tag(new Tag.Builder().value(tag).build());
            return this;
        }
    }

    @Override
    public String toString() {
        return pattern() != null ? tag() + " : " + pattern() : tag().toString();
    }

    @Value.Immutable
    public static abstract class Tag implements Element {
        @Override
        public Kind kind() {
            return Kind.TERMINAL_TAG;
        }

        public abstract String value();

        public static class Builder extends ImmutableTag.Builder {

        }

        @Override
        public String toString() {
            return value();
        }
    }
}
