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
package io.github.tdf4j.parser;

import org.immutables.value.Value;
import org.joor.Reflect;
import io.github.tdf4j.core.model.Dependency;
import io.github.tdf4j.core.model.Environment;
import io.github.tdf4j.core.model.Grammar;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

@Value.Immutable
public abstract class ParserMetaInformation {

    public abstract String getPackage();

    public abstract String[] getImports();

    public abstract Environment getEnvironment();

    public abstract Grammar getGrammar();

    public abstract String getClassName();

    public abstract String getSourceCode();

    public Parser compile() {
        return Reflect.compile(getPackage() + "." + getClassName(), getSourceCode())
                .create(args())
                .get();
    }

    private Object[] args() {
        return Stream.concat(
                Stream.of(getGrammar()),
                Arrays.stream(getEnvironment().getDependencies())
                        .map((Function<Dependency, Object>) Dependency::instance)
        ).toArray(Object[]::new);
    }

    public static class Builder extends ImmutableParserMetaInformation.Builder {
    }

}
