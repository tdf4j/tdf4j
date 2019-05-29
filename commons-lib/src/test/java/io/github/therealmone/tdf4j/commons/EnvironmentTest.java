package io.github.therealmone.tdf4j.commons;

import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentTest {

    @Test
    public void full() {
        final Environment environment = new Environment.Builder()
                .packages("pack1", "pack2")
                .dependencies(new Dependency.Builder<TestDependency>()
                        .clazz(TestDependency.class)
                        .name("test")
                        .build()
                )
                .code("code")
                .build();
        assertEquals("pack1", environment.getPackages()[0]);
        assertEquals("pack2", environment.getPackages()[1]);
        assertEquals(TestDependency.class, environment.getDependencies()[0].clazz());
        assertEquals("test", environment.getDependencies()[0].name());
        assertEquals(TestDependency.class, environment.getDependencies()[0].instance().getClass());
        assertEquals("code", environment.getCode());
    }

    @Test
    public void without_args() {
        final Environment environment = new Environment.Builder().build();
        assertEquals(0, environment.getPackages().length);
        assertEquals(0, environment.getDependencies().length);
        assertEquals("", environment.getCode());
    }


    public static class TestDependency {
        public TestDependency() {}
    }
}
