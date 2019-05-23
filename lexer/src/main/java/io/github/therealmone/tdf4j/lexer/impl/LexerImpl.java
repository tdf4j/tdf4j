/*
 * Copyright 2019 Roman Fatnev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.therealmone.tdf4j.lexer.impl;

import io.github.therealmone.tdf4j.commons.Stream;
import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import io.github.therealmone.tdf4j.model.Token;
import io.github.therealmone.tdf4j.lexer.SymbolListener;
import io.github.therealmone.tdf4j.lexer.UnexpectedSymbolException;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class LexerImpl implements Lexer {
    private final List<Terminal> terminals;
    private final SymbolListener listener;

    public LexerImpl(final AbstractLexerModule config, final SymbolListener listener) {
        this.terminals = config.getTerminals();
        this.listener = listener;
    }

    @Override
    @Nonnull
    public List<Token> analyze(final String input) {
        final List<Token> tokens = new ArrayList<>();
        final Stream<Token> stream = stream(input);
        stream.forEach(tokens::add);
        return tokens;
    }

    @Nonnull
    @Override
    public Stream<Token> stream(final String input) {
        listener.reset();
        final StringBuilder in = new StringBuilder(input);
        return new Stream.Builder<Token>().generator(() -> nextToken(in)).build();
    }

    @Nullable
    private Token nextToken(final StringBuilder in) {
        final StringBuilder buffer = new StringBuilder();
        while(in.length() != 0) {
            buffer.append(in.charAt(buffer.length()));
            if(!anyTerminalHitEnd(buffer) || buffer.length() == in.length()) {
                removeLast(buffer, in);
                final Terminal terminal = tryToSpecifyTerminal(buffer);
                if(terminal != null) {
                    final Token token = tokenFrom(terminal, buffer, in);
                    return terminal.hidden() ? nextToken(in) : token;
                } else {
                    throw exception(buffer, in);
                }
            }
        }
        return null;
    }

    private Token tokenFrom(final Terminal terminal, final StringBuilder buffer, final StringBuilder in) {
        final Token token = new Token.Builder()
                .tag(terminal.tag())
                .value(buffer.toString())
                .build();
        buffer.chars().forEach(ch -> listener.listen((char) ch));
        in.replace(0, buffer.length(), "");
        buffer.replace(0, buffer.length(), "");
        return token;
    }

    private UnexpectedSymbolException exception(final StringBuilder buffer, final StringBuilder in) {
        if(buffer.length() != 0) {
            buffer.chars().forEach(ch -> listener.listen((char) ch));
        } else {
            listener.listen(in.charAt(0));
        }
        return new UnexpectedSymbolException(in.charAt(buffer.length() == 0 ? buffer.length() : buffer.length() - 1), listener.line(), listener.column());
    }

    private void removeLast(final StringBuilder buffer, final StringBuilder in) {
        if(buffer.length() != in.length()) {
            buffer.deleteCharAt(buffer.length() - 1);
        } else if(tryToSpecifyTerminal(buffer) == null) {
            buffer.deleteCharAt(buffer.length() - 1);
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
                .sorted(Comparator.comparingLong(Terminal::priority))
                .collect(Collectors.toList());
        return terminals.size() > 0
                ? terminals.get(terminals.size() - 1)
                : null;
    }
}
