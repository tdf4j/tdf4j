package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.NonTerminal;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;

@Value.Immutable
public interface NonTerminalTemplate extends CodeBlock {

    NonTerminal nonTerminal();

    @Override
    default String build() {
        return Template.LOGIC_NON_TERMINAL.template()
                .add("nonTerminal", nonTerminal().identifier())
                .add("comment", nonTerminal().toString())
                .render();
    }

    class Builder extends ImmutableNonTerminalTemplate.Builder {
    }
}
