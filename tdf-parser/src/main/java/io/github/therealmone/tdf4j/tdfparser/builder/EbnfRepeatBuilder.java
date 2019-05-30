package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.model.ebnf.Repeat;

public class EbnfRepeatBuilder extends AbstractEbnfElementBuilder<Repeat> {

    EbnfRepeatBuilder(BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Repeat build(final ASTNode tree) {
        return new Repeat.Builder()
                .elements(getInnerElements(tree).stream().map(this::callBuilder).toArray(Element[]::new))
                .build();
    }
}
