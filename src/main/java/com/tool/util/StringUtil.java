//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tool.util;

public class StringUtil {
    public StringUtil() {
    }

    public static boolean hasText(String str) {
        return str != null && !str.equals("");
    }

    public static String firstLetterToUpperCase(String str) {
        if (str.charAt(0) >= 'a' && str.charAt(0) <= 'z') {
            str = str.replaceFirst(String.valueOf(str.charAt(0)), String.valueOf(str.charAt(0)).toUpperCase());
        }

        return str;
    }
}
