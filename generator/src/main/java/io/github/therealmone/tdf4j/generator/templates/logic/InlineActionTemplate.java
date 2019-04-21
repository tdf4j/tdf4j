package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.InlineAction;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;

@Value.Immutable
public interface InlineActionTemplate extends CodeBlock {

    InlineAction inline();

    @Override
    default String build() {
        return Template.LOGIC_INLINE_ACTION.template()
                .add("inlineAction", inline().code())
                .render();
    }

    class Builder extends ImmutableInlineActionTemplate.Builder {
    }
}
