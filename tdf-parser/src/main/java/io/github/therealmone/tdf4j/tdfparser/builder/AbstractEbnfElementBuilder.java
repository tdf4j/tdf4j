package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTElement;
import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Element;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractEbnfElementBuilder<T> implements Builder<T> {
    private final BuilderMapper mapper;

    AbstractEbnfElementBuilder(final BuilderMapper mapper) {
        this.mapper = mapper;
    }

    List<ASTNode> getInnerElements(final ASTNode element) {
        final List<ASTNode> elements = new ArrayList<>();
        for(final ASTElement child : element.children()) {
            if(!child.isNode()) {
                continue;
            }
            if(child.asNode().tag().equalsIgnoreCase("ebnf_element")) {
                elements.add(child.asNode());
            } else if(child.asNode().tag().equalsIgnoreCase("ebnf_elements_set")) {
                elements.addAll(getInnerElements(child.asNode()));
            }
        }
        return elements;
    }

    Element callBuilder(final ASTNode ebnfElement) {
        return mapper.build(ebnfElement);
    }

}
