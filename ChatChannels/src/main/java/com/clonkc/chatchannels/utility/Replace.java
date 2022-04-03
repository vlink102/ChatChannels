package com.clonkc.chatchannels.utility;

import org.bukkit.ChatColor;

public class Replace {
    public static String rep(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static String rep(String string, String placeholder, String replace) {
        return ChatColor.translateAlternateColorCodes('&', string.replace(placeholder, replace));
    }
    public static String rep(String string, String placeholder, String replace, String placeholder2, String replace2) {
        return ChatColor.translateAlternateColorCodes('&', string.replace(placeholder,replace).replace(placeholder2, replace2));
    }
    public static String rep(String string, String placeholder, String replace, String placeholder2, String replace2, String placeholder3, String replace3) {
        return ChatColor.translateAlternateColorCodes('&', string.replace(placeholder,replace).replace(placeholder2, replace2).replace(placeholder3,replace3));
    }
    public static String rep(String string, String placeholder, String replace, String placeholder2, String replace2, String placeholder3, String replace3, String placeholder4, String replace4) {
        return ChatColor.translateAlternateColorCodes('&', string.replace(placeholder,replace).replace(placeholder2, replace2).replace(placeholder3,replace3).replace(placeholder4,replace4));
    }
    public static String rep(String string, String placeholder, String replace, String placeholder2, String replace2, String placeholder3, String replace3, String placeholder4, String replace4, String placeholder5, String replace5) {
        return ChatColor.translateAlternateColorCodes('&', string.replace(placeholder,replace).replace(placeholder2, replace2).replace(placeholder3,replace3).replace(placeholder4,replace4).replace(placeholder5, replace5));
    }
    public static String rep(Character translate, String string) {
        return ChatColor.translateAlternateColorCodes(translate, string);
    }
}
