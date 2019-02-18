package io.github.therealmone.tdf4j.lexer;

import org.junit.Test;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;

public class AbstractLexerConfigTest {

    @Test
    public void normal_configuration() {
        final AbstractLexerConfig config = new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("ONE").with("^one$");
                tokenize("TWO").with("^two$");
                tokenize("THREE").with("^three$");
            }
        };

        final List<AbstractLexerConfig.Tokenizer> tokenizers = config.getTokenizers();
        assertEquals(3, tokenizers.size());
        assertEquals("ONE", tokenizers.get(0).getName());
        assertEquals("^one$", tokenizers.get(0).getPattern().pattern());
        assertEquals("TWO", tokenizers.get(1).getName());
        assertEquals("^two$", tokenizers.get(1).getPattern().pattern());
        assertEquals("THREE", tokenizers.get(2).getName());
        assertEquals("^three$", tokenizers.get(2).getPattern().pattern());
    }

    @Test(expected = RuntimeException.class)
    public void not_complete_bindings() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("token").with("pattern");
                tokenize("token2").with("pattern2");
                tokenize("token3");
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void duplicate_names() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("token").with("pattern");
                tokenize("token2").with("pattern2");
                tokenize("token2").with("pattern3");
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void null_name() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize(null).with("pattern");
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void blank_name() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("    ").with("pattern");
            }
        };
    }

    @Test(expected = PatternSyntaxException.class)
    public void not_compilable_pattern() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("token").with("[a]{invalid}");
            }
        };
    }
}
