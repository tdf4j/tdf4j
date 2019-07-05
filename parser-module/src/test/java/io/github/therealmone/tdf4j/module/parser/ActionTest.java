package io.github.therealmone.tdf4j.module.parser;

import io.github.therealmone.tdf4j.model.ebnf.NonTerminal;
import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActionTest {

    @Test
    public void TokenAction() {
        new AbstractParserModule() {
            @Override
            protected void configure() {
                final Terminal.Tag terminal = t("A", "System.out.println(token);");
                assertEquals("A", terminal.getValue());
                assertEquals("System.out.println(token);", terminal.getTokenAction());
            }
        }.build();
    }

    @Test
    public void NodeAction() {
        new AbstractParserModule() {
            @Override
            protected void configure() {
                final NonTerminal nonTerminal = nt("A", "System.out.println(node);");
                assertEquals("A", nonTerminal.getValue());
                assertEquals("System.out.println(node);", nonTerminal.getNodeAction());
            }
        }.build();
    }

}
