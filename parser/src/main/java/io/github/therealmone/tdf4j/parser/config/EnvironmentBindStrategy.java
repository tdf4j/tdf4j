package io.github.therealmone.tdf4j.parser.config;

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
