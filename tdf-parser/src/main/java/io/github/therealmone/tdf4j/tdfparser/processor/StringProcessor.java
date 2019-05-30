package io.github.therealmone.tdf4j.tdfparser.processor;

import javax.annotation.Nullable;

public class StringProcessor implements Processor<String> {

    @Override
    public String process(@Nullable final String element) {
        if(element == null || element.equals("\"\"")) {
            return "";
        }
        if(!element.matches("\"([^\"]|(\\\\\"))*\"")) {
            return element;
        }
        return element
                .substring(1, element.length() - 1)
                .replaceAll("\\\\\"", "\"");
    }

}
