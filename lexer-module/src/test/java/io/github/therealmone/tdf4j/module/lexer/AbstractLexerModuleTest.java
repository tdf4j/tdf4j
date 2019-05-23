package io.github.therealmone.tdf4j.module.lexer;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;

public class AbstractLexerModuleTest {

    @Test
    public void normal_configuration() {
        final AbstractLexerModule config = new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("ONE").pattern("^one$");
                tokenize("TWO").pattern("^two$");
                tokenize("THREE").pattern("^three$");
            }
        }.build();

        final List<Terminal> terminals = config.getTerminals();
        assertEquals(3, terminals.size());
        Assert.assertEquals("ONE", terminals.get(0).tag().value());
        Assert.assertEquals("^one$", terminals.get(0).pattern().pattern());
        Assert.assertEquals("TWO", terminals.get(1).tag().value());
        Assert.assertEquals("^two$", terminals.get(1).pattern().pattern());
        Assert.assertEquals("THREE", terminals.get(2).tag().value());
        Assert.assertEquals("^three$", terminals.get(2).pattern().pattern());
    }

    @Test(expected = RuntimeException.class)
    public void not_complete_bindings() {
        new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("pattern");
                tokenize("token2").pattern("pattern2");
                tokenize("token3");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void duplicate_names() {
        new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("pattern");
                tokenize("token2").pattern("pattern2");
                tokenize("token2").pattern("pattern3");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void null_name() {
        new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize(null).pattern("pattern");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void blank_name() {
        new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("    ").pattern("pattern");
            }
        }.build();
    }

    @Test(expected = PatternSyntaxException.class)
    public void not_compilable_pattern() {
        new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("[a]{invalid}");
            }
        }.build();
    }

    @Test
    public void priority() {
        final AbstractLexerModule config = new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("pattern").priority(100);
            }
        }.build();
        assertEquals(1, config.getTerminals().size());
        assertEquals("token", config.getTerminals().get(0).tag().value());
        assertEquals("pattern", config.getTerminals().get(0).pattern().pattern());
        assertEquals(100, config.getTerminals().get(0).priority());
    }
}
