package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Or;

import java.util.List;

public class EbnfOrBuilder extends AbstractEbnfElementBuilder<Or> {

    EbnfOrBuilder(BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Or build(final ASTNode tree) {
        return oneOf(getInnerElements(tree));
    }

    private Or oneOf(final List<ASTNode> elements) {
        if(elements.size() == 2) {
            return new Or.Builder().first(callBuilder(elements.get(0))).second(callBuilder(elements.get(1))).build();
        } else {
            return new Or.Builder().first(callBuilder(elements.get(0))).second(oneOf(elements.subList(1, elements.size()))).build();
        }
    }

}
