/*
 *
 *  Copyright (c) 2019 tdf4j
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.tdf4j.core.model;

import org.immutables.value.Value;
import io.github.tdf4j.core.model.ebnf.Terminal;

import java.util.regex.Pattern;

import static io.github.tdf4j.core.model.ebnf.EBNFBuilder.*;

@Value.Immutable
public abstract class Letter {

    public abstract Terminal getTag();

    public abstract Pattern getPattern();

    @Value.Default
    public long priority() {
        return 0;
    }

    @Value.Default
    public boolean hidden() {
        return false;
    }

    public static class Builder extends ImmutableLetter.Builder {
        public Letter.Builder pattern(final String pattern) {
            super.setPattern(Pattern.compile(pattern));
            return this;
        }

        public Letter.Builder pattern(final String pattern, final int flags) {
            super.setPattern(Pattern.compile(pattern, flags));
            return this;
        }

        public Letter.Builder tag(final String tag) {
            super.setTag(terminal(tag));
            return this;
        }

        public Letter.Builder tag(final Terminal tag) {
            super.setTag(tag);
            return this;
        }

        public Letter.Builder priority(final long priority) {
            return super.setPriority(priority);
        }

        public Letter.Builder hidden(final boolean hidden) {
            return super.setHidden(hidden);
        }
    }

    @Override
    public String toString() {
        return getPattern() != null ? getTag() + " : " + getPattern() : getTag().toString();
    }


}
