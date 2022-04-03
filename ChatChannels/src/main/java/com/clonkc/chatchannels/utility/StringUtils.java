package com.clonkc.chatchannels.utility;

public class StringUtils {
    public static Boolean checkOneChar(String string) {
        String pattern = "([a-zA-Z])\\1*";

        return string.matches(pattern);
    }
}
