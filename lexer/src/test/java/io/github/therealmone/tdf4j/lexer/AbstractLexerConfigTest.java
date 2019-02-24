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
                tokenize("ONE").pattern("^one$");
                tokenize("TWO").pattern("^two$");
                tokenize("THREE").pattern("^three$");
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
                tokenize("token").pattern("pattern");
                tokenize("token2").pattern("pattern2");
                tokenize("token3");
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void duplicate_names() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("token").pattern("pattern");
                tokenize("token2").pattern("pattern2");
                tokenize("token2").pattern("pattern3");
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void null_name() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize(null).pattern("pattern");
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void blank_name() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("    ").pattern("pattern");
            }
        };
    }

    @Test(expected = PatternSyntaxException.class)
    public void not_compilable_pattern() {
        new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("token").pattern("[a]{invalid}");
            }
        };
    }

    @Test
    public void priority() {
        final AbstractLexerConfig config = new AbstractLexerConfig() {
            @Override
            public void config() {
                tokenize("token").pattern("pattern").priority(100);
            }
        };
        assertEquals(1, config.getTokenizers().size());
        assertEquals("token", config.getTokenizers().get(0).getName());
        assertEquals("pattern", config.getTokenizers().get(0).getPattern().pattern());
        assertEquals(100, config.getTokenizers().get(0).getPriority());
    }
}
