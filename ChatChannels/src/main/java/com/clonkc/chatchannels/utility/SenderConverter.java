package com.clonkc.chatchannels.utility;

import com.clonkc.chatchannels.ChatChannels;
import com.clonkc.chatchannels.Configuration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SenderConverter {

    public static String convert(CommandSender sender) {
        Configuration config = ChatChannels.getInstance().getConfiguration();

        if (sender instanceof Player) {
            return ((Player) sender).getDisplayName();
        } else {
            return config.getConsolePlayer();
        }
    }
}
