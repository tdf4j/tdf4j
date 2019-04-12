package io.github.therealmone.tdf4j.parser.model.ast;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Modifiable
public abstract class ASTNode implements ASTElement {

    @Override
    public Kind kind() {
        return Kind.NODE;
    }

    public abstract ASTElement parent();

    public abstract List<ASTElement> children();

    public abstract String tag();

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(tag());
        for(final ASTElement child : children()) {
            builder.append("\n")
                    .append(collectColumns())
                    .append("|--")
                    .append(child.toString());
        }
        return builder.toString();
    }

    private String collectColumns() {
        final StringBuilder builder = new StringBuilder();
        ASTElement current = this;
        while(!current.isRoot()) {
            builder.append(isLat(current) ? "\t " : "\t|");
            current = current.isLeaf()
                    ? current.asLeaf().parent()
                    : current.asNode().parent();
        }
        return builder.reverse().toString();
    }

    private boolean isLat(final ASTElement element) {
        final ASTElement parent = element.isNode()
                ? element.asNode().parent()
                : element.asLeaf().parent();
        final List<ASTElement> neighbours = parent.isRoot()
                ? parent.asRoot().children()
                : parent.asNode().children();
        final int index = indexOf(neighbours, element);
        return index != -1 && index == neighbours.size() - 1;
    }

    private int indexOf(final List<ASTElement> elements, final ASTElement element) {
        for (int i = 0; i < elements.size(); i++) {
            if(elements.get(i) == element) {
                return i;
            }
        }
        return -1;
    }
}
