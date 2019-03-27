package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.commons.BindStrategy;
import io.github.therealmone.tdf4j.commons.model.ebnf.Production;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionBindStrategy implements BindStrategy<String, Production.Builder, List<Production>> {
    private final Map<String, Production.Builder> prods = new HashMap<>();

    @Override
    public Production.Builder bind(final String key) {
        if(key == null || key.trim().equals("")) {
            throw new RuntimeException("Key can't be blank or null");
        }
        if(prods.containsKey(key)) {
            throw new RuntimeException("Key " + key + "already bind");
        }
        prods.put(key, new Production.Builder().identifier(key));
        return prods.get(key);
    }

    @Override
    public List<Production> build() {
        return new ArrayList<>() {{
            prods.forEach((key, prod) -> add(prod.build()));
        }};
    }
}
