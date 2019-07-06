package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class LexisTest extends TdfParserTest {

    @Test
    public void test() {
        final TdfParser tdfParser = generate("LexisTest.tdf");
        final List<Terminal> terminals = tdfParser.getLexerModule().build().getTerminals();
        System.out.println(terminals);

        assertEquals(33, terminals.size());
        assertTerminal(terminals.get(0), t("PRINT").pattern("print").priority(1));
        assertTerminal(terminals.get(1), t("NEW").pattern("new").priority(1));
        assertTerminal(terminals.get(2), t("REWRITE").pattern("rewrite").priority(1));
        assertTerminal(terminals.get(3), t("QUOTE").pattern("\""));
        assertTerminal(terminals.get(4), t("VAR").pattern("[a-z]+"));
        assertTerminal(terminals.get(5), t("CONCAT").pattern("\\+\\+").priority(2));
        assertTerminal(terminals.get(6), t("DO").pattern("do").priority(1));
        assertTerminal(terminals.get(7), t("ASSIGN_OP").pattern("="));
        assertTerminal(terminals.get(8), t("REMOVE").pattern("remove").priority(1));
        assertTerminal(terminals.get(9), t("ELSE").pattern("else").priority(1));
        assertTerminal(terminals.get(10), t("DOUBLE").pattern("-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))"));
        assertTerminal(terminals.get(11), t("WS").pattern("\\s|\\n|\\r").priority(3).hidden(true));
        assertTerminal(terminals.get(12), t("IF").pattern("if").priority(1));
        assertTerminal(terminals.get(13), t("EOF").pattern("\\$"));
        assertTerminal(terminals.get(14), t("COMMA").pattern(","));
        assertTerminal(terminals.get(15), t("OP").pattern("[\\+\\-/\\*]|(div)|(mod)").priority(1));
        assertTerminal(terminals.get(16), t("ARRAYLIST").pattern("arraylist").priority(1));
        assertTerminal(terminals.get(17), t("COP").pattern("[<>]|(<=)|(>=)|(==)|(!=)").priority(1));
        assertTerminal(terminals.get(18), t("SIZE").pattern("size").priority(1));
        assertTerminal(terminals.get(19), t("FOR").pattern("for").priority(1));
        assertTerminal(terminals.get(20), t("DEL").pattern(";", Pattern.MULTILINE));
        assertTerminal(terminals.get(21), t("HASHSET").pattern("hashset").priority(1));
        assertTerminal(terminals.get(22), t("LOP").pattern("[&\\|\\^\\!]").priority(1));
        assertTerminal(terminals.get(23), t("DIGIT").pattern("-?(0|([1-9][0-9]*))").priority(1));
        assertTerminal(terminals.get(24), t("PUT").pattern("put").priority(1));
        assertTerminal(terminals.get(25), t("RB").pattern("\\)"));
        assertTerminal(terminals.get(26), t("TYPEOF").pattern("typeof").priority(1));
        assertTerminal(terminals.get(27), t("LB").pattern("\\("));
        assertTerminal(terminals.get(28), t("GET").pattern("get").priority(1));
        assertTerminal(terminals.get(29), t("FRB").pattern("\\}"));
        assertTerminal(terminals.get(30), t("STRING").pattern("\"[^\"]*\""));
        assertTerminal(terminals.get(31), t("WHILE").pattern("while").priority(1));
        assertTerminal(terminals.get(32), t("FLB").pattern("\\{"));

    }

    private void assertTerminal(final Terminal terminal, final Terminal.Builder another) {
        final Terminal expected = another.build();
        assertEquals(expected.getTag().getValue(), terminal.getTag().getValue());
        assertEquals(expected.getPattern().pattern(), terminal.getPattern().pattern());
        assertEquals(expected.getPattern().flags(), terminal.getPattern().flags());
        assertEquals(expected.priority(), terminal.priority());
        assertEquals(expected.hidden(), terminal.hidden());
    }

    private Terminal.Builder t(final String tag) {
        return new Terminal.Builder().tag(tag);
    }

}
