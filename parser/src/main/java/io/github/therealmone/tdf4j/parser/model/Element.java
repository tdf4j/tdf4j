package io.github.therealmone.tdf4j.parser.model;

public interface Element {
    Kind kind();

    default boolean isName() {
        return this.kind() == Kind.NAME;
    }

    default Name asName() {
        return (Name) this;
    }

    default boolean isOptional() {
        return this.kind() == Kind.OPTIONAL;
    }

    default Optional asOptional() {
        return (Optional) this;
    }

    default boolean isGroup() {
        return this.kind() == Kind.GROUP;
    }

    default Group asGroup() {
        return (Group) this;
    }

    default boolean isOr() {
        return this.kind() == Kind.OR;
    }

    default Or asOr() {
        return (Or) this;
    }

    default boolean isRepeat() {
        return this.kind() == Kind.REPEAT;
    }

    default Repeat asRepeat() {
        return (Repeat) this;
    }

    default String toStringGroup(final Element ... elements) {
        final StringBuilder builder = new StringBuilder();
        if(elements.length > 0) {
            for (final Element element : elements) {
                builder.append(element.toString()).append(",");
            }
            builder.replace(builder.length() - 1, builder.length(), "");
        }
        return builder.toString();
    }
}
