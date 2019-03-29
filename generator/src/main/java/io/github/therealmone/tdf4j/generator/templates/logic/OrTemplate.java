package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Or;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface OrTemplate extends CodeBlock {

    Or or();

    @Override
    default String build() {
        final ST template = Template.LOGIC_OR.template()
                .add("comment", or().toString())
                .add("hash", Math.abs(this.hashCode()));
        final CodeBlock first = CodeBlock.fromElement(or().first());
        if(first != null) {
            template.add("codeBlocks1", first.build());
        }
        final CodeBlock second = CodeBlock.fromElement(or().second());
        if(second != null) {
            template.add("codeBlocks2", second.build());
        }
        return template.render();
    }

    class Builder extends ImmutableOrTemplate.Builder {
    }
}
