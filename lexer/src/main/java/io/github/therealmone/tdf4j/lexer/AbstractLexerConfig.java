package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.commons.bean.ImmutableTerminal;
import io.github.therealmone.tdf4j.commons.bean.Terminal;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public abstract class AbstractLexerConfig {
    private Map<String, Tokenizer> tokenizers;

    public AbstractLexerConfig() {
        this.tokenizers = new HashMap<>();
        config();
        validate();
    }

    public abstract void config();

    public class Tokenizer {
        private String tag;
        private Pattern pattern;
        private int priority;

        Tokenizer(final String tag) {
            this.tag = tag;
        }

        public Tokenizer pattern(final String pattern) {
            this.pattern = Pattern.compile(pattern);
            return this;
        }

        public Tokenizer priority(final int priority) {
            this.priority = priority;
            return this;
        }

        public String getTag() {
            return tag;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public int getPriority() {
            return priority;
        }
    }

    public Tokenizer tokenize(@Nullable final String name) {
        if(tokenizers.containsKey(name)) {
            throw new RuntimeException("Key " + name + " already bind");
        }

        if(name == null || name.trim().equals("")) {
            throw new RuntimeException("Name can't be null or blank");
        }

        tokenizers.put(name, new Tokenizer(name));
        return tokenizers.get(name);
    }

    private void validate() {
        for(final Tokenizer tokenizer : tokenizers.values()) {
            if(tokenizer.pattern == null) {
                throw new RuntimeException("Pattern was not specified for token " + tokenizer.tag);
            }
        }
    }

    public List<Terminal> getTerminals() {
        return Collections.unmodifiableList(new ArrayList<Terminal>() {{
            for(final Tokenizer tokenizer : tokenizers.values()) {
                add(ImmutableTerminal.builder()
                        .tag(tokenizer.getTag())
                        .pattern(tokenizer.pattern)
                        .priority(tokenizer.priority)
                        .build()
                );
            }
        }});
    }
}
