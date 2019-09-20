/*
 * Copyright (c) 2019 tdf4j
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
package org.tdf4j.lexer;

import org.tdf4j.core.model.Alphabet;
import org.tdf4j.core.model.Letter;
import org.tdf4j.core.model.Stream;
import org.tdf4j.core.model.Token;
import org.tdf4j.core.module.LexerAbstractModule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class LexerImpl implements Lexer {
    private final Alphabet alphabet;
    private final SymbolListener listener;

    LexerImpl(final LexerAbstractModule module, final SymbolListener listener) {
        this.alphabet = module.build().getAlphabet();
        this.listener = listener;
    }

    @Nonnull
    @Override
    public Stream<Token> analyze(final CharSequence input) {
        listener.reset();
        final StringBuilder in = new StringBuilder(input);
        return new Stream.Builder<Token>().setGenerator(() -> nextToken(in)).build();
    }

    @Nullable
    private Token nextToken(final StringBuilder in) {
        final StringBuilder buffer = new StringBuilder();
        while(in.length() != 0) {
            buffer.append(in.charAt(buffer.length()));
            if(!anyLetterHitEnd(buffer) || buffer.length() == in.length()) {
                removeLast(buffer, in);
                final Letter letter = tryToSpecifyLetter(buffer);
                if(letter != null) {
                    final Token token = tokenFrom(letter, buffer, in);
                    return letter.hidden() ? nextToken(in) : token;
                } else {
                    throw exception(buffer, in);
                }
            }
        }
        return null;
    }

    private Token tokenFrom(final Letter letter, final StringBuilder buffer, final StringBuilder in) {
        final Token token = new Token.Builder()
                .setTag(letter.getTag())
                .setValue(buffer.toString())
                .setRow(listener.line())
                .setColumn(listener.column())
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
        } else if(tryToSpecifyLetter(buffer) == null) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
    }

    private boolean anyLetterHitEnd(final StringBuilder buffer) {
        for(final Letter letter : alphabet.getLetters()) {
            final Matcher matcher = letter.getPattern().matcher(buffer);
            if(matcher.matches() || matcher.hitEnd()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private Letter tryToSpecifyLetter(final StringBuilder buffer) {
        final List<Letter> letters = this.alphabet.getLetters().stream()
                .filter(terminal -> terminal.getPattern().matcher(buffer).matches())
                .sorted(Comparator.comparingLong(Letter::priority))
                .collect(Collectors.toList());
        return letters.size() > 0
                ? letters.get(letters.size() - 1)
                : null;
    }
}
