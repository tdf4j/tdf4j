package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.commons.bean.Terminal;
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

        final List<Terminal> terminals = config.getTerminals();
        assertEquals(3, terminals.size());
        assertEquals("ONE", terminals.get(0).tag());
        assertEquals("^one$", terminals.get(0).pattern().pattern());
        assertEquals("TWO", terminals.get(1).tag());
        assertEquals("^two$", terminals.get(1).pattern().pattern());
        assertEquals("THREE", terminals.get(2).tag());
        assertEquals("^three$", terminals.get(2).pattern().pattern());
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
        assertEquals(1, config.getTerminals().size());
        assertEquals("token", config.getTerminals().get(0).tag());
        assertEquals("pattern", config.getTerminals().get(0).pattern().pattern());
        assertEquals(100, config.getTerminals().get(0).priority());
    }
}
