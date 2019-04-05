package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Element;
import io.github.therealmone.tdf4j.commons.model.ebnf.Optional;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface OptionalTemplate extends CodeBlock, Prediction {

    Optional optional();

    @Override
    default String build() {
        final ST template = Template.LOGIC_OPTIONAL.template()
                .add("comment", optional().toString())
                .add("hash", Math.abs(this.hashCode()));
        for(final Element element : optional().elements()) {
            final CodeBlock codeBlock = CodeBlock.fromElement(element);
            if(codeBlock != null) {
                template.add("codeBlocks", codeBlock.build());
            }
        }
        if(optional().elements().length != 0) {
            template.add("firstElements", getStartElements(optional().elements()[0]));
        }
        return template.render();
    }

    class Builder extends ImmutableOptionalTemplate.Builder {
    }
}
