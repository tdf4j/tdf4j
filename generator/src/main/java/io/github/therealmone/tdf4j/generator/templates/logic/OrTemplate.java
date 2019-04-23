package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Or;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface OrTemplate extends CodeBlock, Prediction {

    Or or();

    @Override
    default String build() {
        final ST template = Template.LOGIC_OR.template();
        final CodeBlock first = CodeBlock.fromElement(or().first());
        if(first != null) {
            template.add("firstStartElements", getStartElements(or().first()));
            template.add("firstCodeBlocks", first.build());
        }
        final CodeBlock second = CodeBlock.fromElement(or().second());
        if(second != null) {
            template.add("secondStartElements", getStartElements(or().second()));
            template.add("secondCodeBlocks", second.build());
        }
        return template.render();
    }

    class Builder extends ImmutableOrTemplate.Builder {
    }
}
