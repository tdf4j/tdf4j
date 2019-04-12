package io.github.therealmone.tdf4j.parser.model.ast;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Modifiable
public abstract class ASTRoot implements ASTElement {

    @Override
    public Kind kind() {
        return Kind.ROOT;
    }

    public abstract List<ASTElement> children();

    public abstract String tag();

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(tag());
        for(final ASTElement child : children()) {
            builder.append("\n║\n")
                    .append("╠┉")
                    .append(child.toString());
        }
        return builder.toString();
    }
}
