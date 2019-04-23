package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Terminal;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;


@Value.Immutable
public interface TerminalTagTemplate extends CodeBlock {

    Terminal.Tag terminalTag();

    @Override
    default String build() {
        return Template.LOGIC_TERMINAL.template()
                .add("terminal", terminalTag().value())
                .render();
    }

    class Builder extends ImmutableTerminalTagTemplate.Builder {
    }
}
