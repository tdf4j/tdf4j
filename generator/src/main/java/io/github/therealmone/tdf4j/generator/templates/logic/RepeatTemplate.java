package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Element;
import io.github.therealmone.tdf4j.commons.model.ebnf.Repeat;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface RepeatTemplate extends CodeBlock, Prediction {

    Repeat repeat();

    @Override
    default String build() {
        final ST template = Template.LOGIC_REPEAT.template()
                .add("comment", repeat().toString())
                .add("hash", Math.abs(this.hashCode()));
        for(final Element element : repeat().elements()) {
            final CodeBlock codeBlock = CodeBlock.fromElement(element);
            if (codeBlock != null) {
                template.add("codeBlocks", codeBlock.build());
            }
        }
        if(repeat().elements().length != 0) {
            template.add("firstElements", getStartElements(repeat().elements()[0]));
        }
        return template.render();
    }

    class Builder extends ImmutableRepeatTemplate.Builder {
    }
}
