package io.github.therealmone.tdf4j.tdfparser.constructor;

import io.github.therealmone.tdf4j.commons.Environment;

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
