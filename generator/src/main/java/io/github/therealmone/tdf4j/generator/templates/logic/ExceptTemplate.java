package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Except;
import io.github.therealmone.tdf4j.commons.model.ebnf.Terminal;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface ExceptTemplate extends CodeBlock {

    Except except();

    @Override
    default String build() {
        final ST template = Template.LOGIC_EXCEPT.template();
        for(final Terminal.Tag tag : except().tags()) {
            template.add("exceptions", tag.value());
        }
        return template.render();
    }

    class Builder extends ImmutableExceptTemplate.Builder {
    }
}
