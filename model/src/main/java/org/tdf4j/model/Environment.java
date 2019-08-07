/*
 * Copyright (c) 2019 Roman Fatnev
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
package org.tdf4j.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Environment {

    @Value.Default
    default String[] getPackages() {
        return new String[]{};
    }

    @Value.Default
    default Dependency[] getDependencies() {
        return new Dependency[]{};
    }

    @Value.Default
    default String getCode() {
        return "";
    }

    class Builder extends ImmutableEnvironment.Builder {
        public Builder packages(@SyntaxHighlight.EnvironmentImports final String... packages) {
            return super.setPackages(packages);
        }

        public Builder code(@SyntaxHighlight.EnvironmentCode final String code) {
            return super.setCode(code);
        }
    }
}
