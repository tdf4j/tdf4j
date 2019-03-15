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
                    .priority((long) terminal.getOrDefault("priority", 0L));
        }
    }
}
