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
package io.github.therealmone.tdf4j.model;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import org.immutables.value.Value;

@Value.Immutable
public interface Token {
    Terminal.Tag getTag();

    String getValue();

    @Value.Default
    default long getRow() {
        return 0;
    }

    @Value.Default
    default long getColumn() {
        return 0;
    }

    class Builder extends ImmutableToken.Builder {
        public Builder setTag(final String tag) {
            return super.setTag(new Terminal.Tag.Builder().setValue(tag).build());
        }
    }
}
