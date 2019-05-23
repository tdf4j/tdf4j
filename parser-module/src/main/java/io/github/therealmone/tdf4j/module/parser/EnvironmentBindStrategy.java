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
package io.github.therealmone.tdf4j.module.parser;

import io.github.therealmone.tdf4j.commons.BindStrategy;
import io.github.therealmone.tdf4j.commons.Environment;

public class EnvironmentBindStrategy implements BindStrategy.WithoutArgs<Environment.Builder, Environment> {
    private Environment.Builder environment;

    @Override
    public Environment.Builder bind() {
        if(environment != null) {
            throw new RuntimeException("Environment can't be bind multiple times");
        }
        this.environment = new Environment.Builder();
        return environment;
    }

    @Override
    public Environment build() {
        if(environment == null) {
            environment = new Environment.Builder()
                    .packages()
                    .dependencies();
        }
        return environment.build();
    }
}
