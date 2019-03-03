package io.github.therealmone.tdf4j.lexer.impl;

import io.github.therealmone.tdf4j.commons.bean.ImmutableToken;
import io.github.therealmone.tdf4j.commons.bean.Terminal;
import io.github.therealmone.tdf4j.commons.bean.Token;
import io.github.therealmone.tdf4j.lexer.AbstractLexerConfig;
import io.github.therealmone.tdf4j.lexer.Lexer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class LexerImpl implements Lexer {
    private final List<Terminal> terminals;

    public LexerImpl(final AbstractLexerConfig config) {
        this.terminals = config.getTerminals();
    }

    @Override
    @Nonnull
    public List<Token> analyze(final String input) {
        final List<Token> tokens = new ArrayList<>();
        final StringBuilder in = new StringBuilder(input);
        final StringBuilder buffer = new StringBuilder();
        while(in.length() != 0) {
            trim(in);
            buffer.append(in.charAt(buffer.length()));
            if(!anyTerminalHitEnd(buffer) || buffer.length() == in.length()) {
                if(buffer.length() != in.length()) {
                    buffer.deleteCharAt(buffer.length() - 1);
                }
                final Terminal terminal = tryToSpecifyTerminal(buffer);
                if(terminal != null) {
                    tokens.add(ImmutableToken.builder()
                            .tag(terminal.tag())
                            .value(buffer.toString())
                            .build()
                    );
                    in.replace(0, buffer.length(), "");
                    buffer.replace(0, buffer.length(), "");
                } else {
                    throw new RuntimeException("Unexpected symbol: " + in.charAt(buffer.length()));
                }
            }
        }
        return tokens;
    }

    private void trim(final StringBuilder builder) {
        trimLeading(builder);
        trimFollowing(builder);
    }

    private void trimLeading(final StringBuilder builder) {
        while(builder.length() > 0 && builder.charAt(builder.length() - 1) == ' ') {
            builder.replace(builder.length() - 1, builder.length(), "");
        }
    }

    private void trimFollowing(final StringBuilder builder) {
        while(builder.length() > 0 && builder.charAt(0) == ' ') {
            builder.replace(0, 1, "");
        }
    }

    private boolean anyTerminalHitEnd(final StringBuilder buffer) {
        for(final Terminal terminal : terminals) {
            final Matcher matcher = terminal.pattern().matcher(buffer);
            if(matcher.matches() || matcher.hitEnd()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private Terminal tryToSpecifyTerminal(final StringBuilder buffer) {
        final List<Terminal> terminals = this.terminals.stream()
                .filter(terminal -> terminal.pattern().matcher(buffer).matches())
                .sorted(Comparator.comparingInt(Terminal::priority))
                .collect(Collectors.toList());
        return terminals.size() > 0
                ? terminals.get(terminals.size() - 1)
                : null;
    }
}
