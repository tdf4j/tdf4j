package org.tdf4j.tdfparser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TdfParserUtils {
    public static void createClass(final String dir, final String fileName, final String code){
        final File file = new File(dir, fileName + ".java");
        if(file.exists()) {
            throw new IllegalArgumentException("File '" + file.getName() + "' already exists");
        }
        try (final FileWriter writer = new FileWriter(file)) {
            if(!file.createNewFile()) {
                throw new IOException("Can't create new file '" + file.getName() + "'");
            }
            writer.write(code);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
