/*
 * Copyright Roman Fatnev
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
package io.github.therealmone.tdf4j.lexer.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonLexerModule extends AbstractLexerModule {
    private final JSONObject json;

    public JsonLexerModule(final String json) throws ParseException {
        this.json = (JSONObject) new JSONParser().parse(json);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        final JSONArray terminals = (JSONArray) json.get("terminals");
        for(final Object o : terminals) {
            final JSONObject terminal = (JSONObject) o;
            tokenize((String) terminal.get("tag"))
                    .pattern((String) terminal.get("pattern"))
                    .priority((long) terminal.getOrDefault("priority", 0L))
                    .hidden((boolean) terminal.getOrDefault("hidden", false));
        }
    }
}
