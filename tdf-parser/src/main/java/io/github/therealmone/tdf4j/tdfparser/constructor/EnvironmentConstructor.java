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
package io.github.therealmone.tdf4j.tdfparser.constructor;

import io.github.therealmone.tdf4j.model.Environment;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentConstructor implements Constructor {
    private final Environment.Builder builder;
    private final List<String> packages = new ArrayList<>();
    private String code;

    public EnvironmentConstructor(final Environment.Builder builder) {
        this.builder = builder;
    }

    @Override
    public void construct() {
        builder.packages(packages.toArray(new String[]{}));
        if(code != null && !code.trim().equalsIgnoreCase("")) {
            builder.code(code);
        }
    }

    public void addPackage(final String pack) {
        this.packages.add(pack);
    }

    public void setCode(String code) {
        this.code = code;
    }
}
