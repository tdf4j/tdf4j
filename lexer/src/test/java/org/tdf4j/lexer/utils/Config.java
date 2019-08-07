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

package org.tdf4j.lexer.utils;

import org.tdf4j.module.lexer.AbstractLexerModule;

import java.util.regex.Pattern;

public class Config extends AbstractLexerModule {
    @Override
    public void configure() {
        tokenize("VAR").pattern("^[a-z]+$");
        tokenize("STRING").pattern("^\"[^\"]*\"$");
        tokenize("NEW").pattern("^new$").priority(1);
        tokenize("TYPEOF").pattern("^typeof$").priority(1);
        tokenize("HASHSET").pattern("^hashset$").priority(1);
        tokenize("ARRAYLIST").pattern("^arraylist$").priority(1);
        tokenize("GET").pattern("^get$").priority(1);
        tokenize("SIZE").pattern("^size$").priority(1);
        tokenize("PUT").pattern("^put$").priority(1);
        tokenize("REMOVE").pattern("^remove$").priority(1);
        tokenize("REWRITE").pattern("^rewrite$").priority(1);
        tokenize("PRINT").pattern("^print$").priority(1);
        tokenize("COMMA").pattern("^,$");
        tokenize("CONCAT").pattern("^\\+\\+$").priority(2);
        tokenize("QUOTE").pattern("^\"$");
        tokenize("DIGIT").pattern("^-?(0|([1-9][0-9]*))$").priority(1);
        tokenize("DOUBLE").pattern("^-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))$");
        tokenize("ASSIGN_OP").pattern("^=$");
        tokenize("OP").pattern("^[\\+\\-\\/\\*]|(div)|(mod)$").priority(1);
        tokenize("DEL").pattern("^;$", Pattern.MULTILINE);
        tokenize("WHILE").pattern("^while$").priority(1);
        tokenize("IF").pattern("^if$").priority(1);
        tokenize("ELSE").pattern("^else$").priority(1);
        tokenize("DO").pattern("^do$").priority(1);
        tokenize("FOR").pattern("^for$").priority(1);
        tokenize("LOP").pattern("^[&\\|\\^\\!]$").priority(1);
        tokenize("COP").pattern("^[<>]|(<=)|(>=)|(==)|(!=)$").priority(1);
        tokenize("LB").pattern("^\\($");
        tokenize("RB").pattern("^\\)$");
        tokenize("FLB").pattern("^\\{$");
        tokenize("FRB").pattern("^\\}$");
        tokenize("$").pattern("^\\$$");
        tokenize("ws").pattern("\\s|\\n|\\r").priority(Integer.MAX_VALUE).hidden(true);
    }
}
