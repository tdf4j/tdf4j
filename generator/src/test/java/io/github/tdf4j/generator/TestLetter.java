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

package io.github.tdf4j.generator;

import io.github.tdf4j.core.model.Letter;

public enum TestLetter {
    A("A", 0),
    B("B", 0),
    C("C", 0);

    private final Letter letter;

    TestLetter(final String tag, final long priority) {
        this.letter = new Letter.Builder().tag(tag).pattern(tag).priority(priority).build();
    }

    public Letter getLetter() {
        return letter;
    }
}
