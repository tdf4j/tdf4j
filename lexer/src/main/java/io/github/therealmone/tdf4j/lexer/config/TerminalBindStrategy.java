package io.github.therealmone.tdf4j.lexer.config.strategies;

import io.github.therealmone.tdf4j.commons.Terminal;

import javax.annotation.Nullable;
import java.util.*;

public class TerminalBindStrategy implements BindStrategy<String, Terminal.Builder, List<Terminal>> {
    private final Map<String, Terminal.Builder> builders;

    public TerminalBindStrategy() {
        this.builders = new HashMap<>();
    }

    @Override
    public Terminal.Builder bind(@Nullable String tag) {
        if(builders.containsKey(tag)) {
            throw new RuntimeException("Key " + tag + " already bind");
        }

        if(tag == null || tag.trim().equals("")) {
            throw new RuntimeException("Tag can't be null or blank");
        }

        builders.put(tag, new Terminal.Builder().tag(tag));
        return builders.get(tag);
    }

    @Override
    public List<Terminal> build() {
        return Collections.unmodifiableList(new ArrayList<Terminal>() {{
            builders.forEach((s, builder) -> add(builder.build()));
        }});
    }
}
