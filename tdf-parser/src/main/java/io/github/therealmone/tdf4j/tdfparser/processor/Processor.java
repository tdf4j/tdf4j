package io.github.therealmone.tdf4j.tdfparser.processor;

public interface Processor<T> {

    T process(final T element);

}
