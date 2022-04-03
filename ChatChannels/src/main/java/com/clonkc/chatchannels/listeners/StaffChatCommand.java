package com.clonkc.chatchannels.listeners;

import com.clonkc.chatchannels.ChatChannels;
import com.clonkc.chatchannels.Configuration;
import com.clonkc.chatchannels.utility.LocationUtils;
import com.clonkc.chatchannels.utility.Replace;
import com.clonkc.chatchannels.utility.SenderConverter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.CommandHook;

public class StaffChatCommand {
    private final ChatChannels plugin;

    public StaffChatCommand(ChatChannels plugin) {
        this.plugin = plugin;
    }

    @CommandHook("staffchat")
    public void onStaffChat(CommandSender sender, String message) {
        Configuration configuration = plugin.getConfiguration();
        NamespacedKey key = plugin.getKey();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("chatchannels.see.staffchat")) {
                if (sender instanceof Player) {
                    player.sendMessage(Replace.rep(configuration.getStaffFormat(), "%player%", SenderConverter.convert(sender), "%message%", message, "%world%", ((Player) sender).getWorld().getName(), "%coordinates%", LocationUtils.convert(((Player) sender).getLocation())));
                } else {
                    player.sendMessage(Replace.rep(configuration.getStaffFormat(), "%player%", SenderConverter.convert(sender), "%message%", message), "%world%", "nowhere", "%coordinates%", "nowhere");
                }
            }
        }
    }
}
