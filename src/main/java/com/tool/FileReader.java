//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileReader {
    public FileReader() {
    }

    public static String read(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
        StringBuilder builder = new StringBuilder();

        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            builder.append(line);
            builder.append("\n");
        }

        return builder.toString();
    }
}
