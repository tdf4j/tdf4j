package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.model.ebnf.Grammar;
import io.github.therealmone.tdf4j.model.ebnf.Production;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntaxTest extends TdfParserTest {

    @Test
    public void test() {
        final TdfParser tdfParser = generate("SyntaxTest.tdf");
        System.out.println(tdfParser.getParserModule().build().getGrammar());
        final Grammar grammar = tdfParser.getParserModule().getGrammar();

        assertEquals(8, grammar.productions().size());

        //[0] - or
        {
            final Production production = grammar.productions().get(0);
            assertEquals("or", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals("A", production.elements().get(0).asOr().first().asTerminalTag().value());
            assertEquals("non_terminal", production.elements().get(0).asOr().second().asNonTerminal().identifier());
        }

        //[1] - non_terminal
        {
            final Production production = grammar.productions().get(1);
            assertEquals("non_terminal", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals("terminal", production.elements().get(0).asNonTerminal().identifier());
        }

        //[2] - repeat
        {
            final Production production = grammar.productions().get(2);
            assertEquals("repeat", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals(2, production.elements().get(0).asRepeat().elements().length);
            assertEquals("A", production.elements().get(0).asRepeat().elements()[0].asTerminalTag().value());
            assertEquals("non_terminal", production.elements().get(0).asRepeat().elements()[1].asNonTerminal().identifier());
        }

        //[3] - inline_action
        {
            final Production production = grammar.productions().get(3);
            assertEquals("inline_action", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals("System.out.println(\"test\");", production.elements().get(0).asInlineAction().code());
        }

        //[4] - optional
        {
            final Production production = grammar.productions().get(4);
            assertEquals("optional", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals("A", production.elements().get(0).asOptional().elements()[0].asTerminalTag().value());
            assertEquals("non_terminal", production.elements().get(0).asOptional().elements()[1].asNonTerminal().identifier());
        }

        //[5] - terminal
        {
            final Production production = grammar.productions().get(5);
            assertEquals("terminal", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals("A", production.elements().get(0).asTerminalTag().value());
        }

        //[6] - repetition
        {
            final Production production = grammar.productions().get(6);
            assertEquals("repetition", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals(3, production.elements().get(0).asRepetition().times());
            assertEquals("A", production.elements().get(0).asRepetition().element().asGroup().elements()[0].asTerminalTag().value());
            assertEquals("non_terminal", production.elements().get(0).asRepetition().element().asGroup().elements()[1].asNonTerminal().identifier());
        }

        //[7] - group
        {
            final Production production = grammar.productions().get(7);
            assertEquals("group", production.identifier().identifier());
            assertEquals(1, production.elements().size());
            assertEquals("A", production.elements().get(0).asGroup().elements()[0].asTerminalTag().value());
            assertEquals("non_terminal", production.elements().get(0).asGroup().elements()[1].asNonTerminal().identifier());
        }
    }
}
