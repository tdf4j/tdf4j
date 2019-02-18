package io.github.therealmone.tdf4j.lexer;

import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractLexerConfig {
    private Map<String, Tokenizer> tokenizers;

    public AbstractLexerConfig() {
        this.tokenizers = new HashMap<>();
        config();
        validate();
    }

    public abstract void config();

    public class Tokenizer {
        private String name;
        private Pattern pattern;

        Tokenizer(final String name) {
            this.name = name;
        }

        public void with(final String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public String getName() {
            return name;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public Tokenizer tokenize(final String name) {
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
                throw new RuntimeException("Pattern was not specified for token " + tokenizer.name);
            }
        }
    }

    public List<Tokenizer> getTokenizers() {
        return new ArrayList<>(tokenizers.values());
    }
}
