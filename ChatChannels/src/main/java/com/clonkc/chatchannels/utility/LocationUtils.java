package com.clonkc.chatchannels.utility;

import org.bukkit.Location;

public class LocationUtils {
    public static String convert(Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
}
