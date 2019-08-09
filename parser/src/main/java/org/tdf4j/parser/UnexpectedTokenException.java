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
package org.tdf4j.parser;

import org.tdf4j.core.model.Token;

import javax.annotation.Nullable;
import java.util.Arrays;

public class UnexpectedTokenException extends RuntimeException {

    public UnexpectedTokenException(@Nullable final Token token, final String... expected) {
        this(token != null
            ? "Unexpected token: " + token + ". Expected: " + Arrays.asList(expected)
            : "Unexpected end of file. Expected: " + Arrays.asList(expected)
        );
    }

    public UnexpectedTokenException(@Nullable final Token token) {
        this(token != null
                ? "Unexpected token: " + token
                : "Unexpected end of file"
        );
    }

    public UnexpectedTokenException(final String message) {
        super(message);
    }

}
