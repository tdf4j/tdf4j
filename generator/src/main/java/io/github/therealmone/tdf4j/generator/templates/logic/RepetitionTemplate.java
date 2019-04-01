package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Repetition;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface RepetitionTemplate extends CodeBlock {

    Repetition repetition();

    @Override
    default String build() {
        final ST template = Template.LOGIC_REPETITION.template()
                .add("comment", repetition().toString())
                .add("hash", Math.abs(this.hashCode()))
                .add("times", repetition().times());
        final CodeBlock codeBlock = CodeBlock.fromElement(repetition().element());
        if(codeBlock != null) {
            template.add("codeBlocks", codeBlock.build());
        }
        return template.render();
    }

    class Builder extends ImmutableRepetitionTemplate.Builder {
    }
}
