package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.model.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LexerTest {
    private final Lexer lexer = new LexerGenerator(new TdfLexerModule()).generate();

    @Test
    public void eof() {
        assertLexerReturns("$",
                token("EOF", "$"));
    }

    @Test
    public void key_lexis() {
        assertLexerReturns("lexis",
                token("KEY_LEXIS", "lexis"));
    }

    @Test
    public void terminal_tag() {
        assertLexerReturns("TERMINAL_09",
                token("TERMINAL_TAG", "TERMINAL_09"));
    }

    @Test
    public void string() {
        assertLexerReturns("\" \\\" this is string \\\" \"",
                token("STRING", "\" \\\" this is string \\\" \""));
    }

    @Test
    public void right_square_bracket() {
        assertLexerReturns("]",
                token("RIGHT_SQUARE_BRACKET", "]"));
    }

    @Test
    public void left_square_bracket() {
        assertLexerReturns("[",
                token("LEFT_SQUARE_BRACKET", "["));
    }

    @Test
    public void integer() {
        assertLexerReturns("0",
                token("INTEGER", "0"));
        assertLexerReturns("1239123903120",
                token("INTEGER", "1239123903120"));
        assertLexerReturns("0123",
                token("INTEGER", "0"),
                token("INTEGER", "123"));
        assertLexerReturns("-100",
                token("INTEGER", "-100"));
    }

    @Test
    public void colon() {
        assertLexerReturns(":",
                token("COLON", ":"));
    }

    @Test
    public void bool() {
        assertLexerReturns("true",
                token("BOOLEAN", "true"));
        assertLexerReturns("false",
                token("BOOLEAN", "false"));
        assertLexerReturns("TRUE",
                token("BOOLEAN", "TRUE"));
        assertLexerReturns("FALSE",
                token("BOOLEAN", "FALSE"));
    }

    @Test
    public void comma() {
        assertLexerReturns(",",
                token("COMMA", ","));
    }

    @Test
    public void terminal_parameter_priority() {
        assertLexerReturns("priority",
                token("TERMINAL_PARAMETER_PRIORITY", "priority"));
    }

    @Test
    public void terminal_parameter_hidden() {
        assertLexerReturns("hidden",
                token("TERMINAL_PARAMETER_HIDDEN", "hidden"));
    }

    @Test
    public void terminal_parameter_pattern_flag() {
        assertLexerReturns("pattern",
                token("TERMINAL_PARAMETER_PATTERN_FLAG", "pattern"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_unix_lines() {
        assertLexerReturns("UNIX_LINES",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES", "UNIX_LINES"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_case_insensitive() {
        assertLexerReturns("CASE_INSENSITIVE",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE", "CASE_INSENSITIVE"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_comments() {
        assertLexerReturns("COMMENTS",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS", "COMMENTS"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_multiline() {
        assertLexerReturns("MULTILINE",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE", "MULTILINE"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_literal() {
        assertLexerReturns("LITERAL",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL", "LITERAL"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_dotall() {
        assertLexerReturns("DOTALL",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL", "DOTALL"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_unicode_case() {
        assertLexerReturns("UNICODE_CASE",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE", "UNICODE_CASE"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_canon_eq() {
        assertLexerReturns("CANON_EQ",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ", "CANON_EQ"));
    }

    @Test
    public void terminal_parameter_pattern_flag_value_unicode_character_class() {
        assertLexerReturns("UNICODE_CHARACTER_CLASS",
                token("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS", "UNICODE_CHARACTER_CLASS"));
    }

    @Test
    public void key_env() {
        assertLexerReturns("env",
                token("KEY_ENV", "env"));
    }

    @Test
    public void hidden_parameter() {
        assertLexerReturns("WS        \"\\s|\\n|\\r\"                                   [priority: 3, hidden: true]",
                token("TERMINAL_TAG", "WS"),
                token("STRING", "\"\\s|\\n|\\r\""),
                token("LEFT_SQUARE_BRACKET", "["),
                token("TERMINAL_PARAMETER_PRIORITY", "priority"),
                token("COLON", ":"),
                token("INTEGER", "3"),
                token("COMMA", ","),
                token("TERMINAL_PARAMETER_HIDDEN", "hidden"),
                token("COLON", ":"),
                token("BOOLEAN", "true"),
                token("RIGHT_SQUARE_BRACKET", "]"));
    }

    @Test
    public void key_import() {
        assertLexerReturns("import",
                token("KEY_IMPORT", "import"));
    }

    @Test
    public void key_code() {
        assertLexerReturns("code",
                token("KEY_CODE", "code"));
    }

    @Test
    public void key_syntax() {
        assertLexerReturns("syntax",
                token("KEY_SYNTAX", "syntax"));
    }

    @Test
    public void delimiter() {
        assertLexerReturns(";",
                token("DELIMITER", ";"));
    }

    @Test
    public void non_terminal() {
        assertLexerReturns("non_terminal_09",
                token("NON_TERMINAL", "non_terminal_09"));
    }

    @Test
    public void lop_or() {
        assertLexerReturns("|",
                token("LOP_OR", "|"));
    }

    @Test
    public void left_figure_bracket() {
        assertLexerReturns("{",
                token("LEFT_FIGURE_BRACKET", "{"));
    }

    @Test
    public void right_figure_bracket() {
        assertLexerReturns("}",
                token("RIGHT_FIGURE_BRACKET", "}"));
    }

    @Test
    public void op_multiply() {
        assertLexerReturns("*",
                token("OP_MULTIPLY", "*"));
    }

    @Test
    public void left_bracket() {
        assertLexerReturns("(",
                token("LEFT_BRACKET", "("));
    }

    @Test
    public void right_bracket() {
        assertLexerReturns(")",
                token("RIGHT_BRACKET", ")"));
    }

    @Test
    public void left_inline_action_bracket() {
        assertLexerReturns("<",
                token("LEFT_INLINE_ACTION_BRACKET", "<"));
    }

    @Test
    public void right_inline_action_bracket() {
        assertLexerReturns(">",
                token("RIGHT_INLINE_ACTION_BRACKET", ">"));
    }

    @Test
    public void op_assign() {
        assertLexerReturns("=",
                token("OP_ASSIGN", "="));
    }

    @Test
    public void op_sum() {
        assertLexerReturns("+",
                token("OP_SUM", "+"));
    }

    @Test
    public void multiline_comment() {
        assertLexerReturns("/* multiline \r\n comment \n */ \n +",
                token("OP_SUM", "+"));
    }

    private void assertLexerReturns(final String input, final Token... tokens) {
        final List<Token> fromLexer = lexer.analyze(input);
        assertEquals(tokens.length, fromLexer.size());
        for (int i = 0; i < tokens.length; i++) {
            assertEquals(tokens[i].getTag().getValue(), fromLexer.get(i).getTag().getValue());
            assertEquals(tokens[i].getValue(), fromLexer.get(i).getValue());
        }
    }

    private Token token(final String tag, final String value) {
        return new Token.Builder().setTag(tag).setValue(value).build();
    }

}
