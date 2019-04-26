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
package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.commons.Stream;
import io.github.therealmone.tdf4j.commons.Token;

import javax.annotation.Nonnull;
import java.util.List;

public interface Lexer {
    @Nonnull
    List<Token> analyze(final String input);

    @Nonnull
    Stream<Token> stream(final String input);
}
