package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Repetition;

public class EbnfRepetitionBuilder extends AbstractEbnfElementBuilder<Repetition> {

    EbnfRepetitionBuilder(final BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Repetition build(final ASTNode tree) {
        return new Repetition.Builder()
                .times(Integer.parseInt(tree.children().get(0).asLeaf().token().value()))
                .element(callBuilder(tree.children().get(2).asNode()))
                .build();
    }

}
