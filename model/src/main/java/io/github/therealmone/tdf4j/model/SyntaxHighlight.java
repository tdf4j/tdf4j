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
package io.github.therealmone.tdf4j.model;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface SyntaxHighlight {

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
    @Language(value = "java", prefix = "package pack; import ", suffix = ";")
    @interface EnvironmentImports {}

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
    @Language(value = "java", prefix = "public class Clazz {", suffix = "}")
    @interface EnvironmentCode {}

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
    @Language(value = "java", prefix = "public class Clazz { public void method() {", suffix = "}}")
    @interface InlineAction {}

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
    @Language(value = "java", prefix = "public class Clazz { public void method() { ((Consumer<Token>) token -> {", suffix = "})}}")
    @interface TokenAction {}

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
    @Language(value = "java", prefix = "public class Clazz { public void method() { ((Consumer<ASTNode>) node -> {", suffix = "})}}")
    @interface NodeAction {}

}
