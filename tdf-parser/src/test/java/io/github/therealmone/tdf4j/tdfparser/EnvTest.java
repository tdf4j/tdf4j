package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.model.Environment;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvTest extends TdfParserTest {

    @Test
    public void test() {
        final TdfParser tdfParser = generate("EnvTest.tdf");
        final Environment environment = tdfParser.getParserModule().build().getEnvironment();

        assertEquals(2, environment.getPackages().length);
        assertEquals("io.github.therealmone.tdf4j.model.Token", environment.getPackages()[0]);
        assertEquals("io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule", environment.getPackages()[1]);
        assertEquals(0, environment.getDependencies().length);
        assertEquals("" +
                "        public String test() {" +
                "            return \"\";" +
                "        }" +
                "    ", environment.getCode().replaceAll("[\r\n]", ""));
    }

}
