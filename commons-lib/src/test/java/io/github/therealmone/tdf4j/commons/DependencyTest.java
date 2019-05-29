package io.github.therealmone.tdf4j.commons;

import org.junit.Test;

import static org.junit.Assert.*;

public class DependencyTest {

    @Test
    public void full() {
        final TestDependency testDependency = new TestDependency();
        final Dependency<TestDependency> dependency = new Dependency.Builder<TestDependency>()
                .clazz(TestDependency.class)
                .name("test")
                .instance(testDependency)
                .build();
        assertEquals(TestDependency.class, dependency.getClazz());
        assertEquals("test", dependency.getName());
        assertEquals(testDependency, dependency.instance());
    }

    @Test
    public void without_instance() {
        final Dependency<TestDependency> dependency = new Dependency.Builder<TestDependency>()
                .clazz(TestDependency.class)
                .name("test")
                .build();
        assertEquals(TestDependency.class, dependency.clazz());
        assertEquals("test", dependency.name());
        assertEquals(TestDependency.class, dependency.instance().getClass());
    }

    @Test
    public void without_instance_and_default_constructor() {
        try {
            new Dependency.Builder<TestDependencyWithoutDefaultConstructor>()
                    .clazz(TestDependencyWithoutDefaultConstructor.class)
                    .name("test")
                    .build();
            fail("Excepted exception");
        } catch (RuntimeException e) {
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertEquals("io.github.therealmone.tdf4j.commons.DependencyTest$TestDependencyWithoutDefaultConstructor.<init>()", e.getCause().getMessage());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class TestDependency {
        public TestDependency() {}
    }

    public static class TestDependencyWithoutDefaultConstructor {
        public TestDependencyWithoutDefaultConstructor(final int value) {}
    }
}
