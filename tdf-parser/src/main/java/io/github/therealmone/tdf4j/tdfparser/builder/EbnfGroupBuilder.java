package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.model.ebnf.Group;

public class EbnfGroupBuilder extends AbstractEbnfElementBuilder<Group> {

    EbnfGroupBuilder(final BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Group build(final ASTNode tree) {
        return new Group.Builder()
                .elements(getInnerElements(tree).stream().map(this::callBuilder).toArray(Element[]::new))
                .build();
    }
}
