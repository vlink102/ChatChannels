package com.clonkc.chatchannels.listeners;

import com.clonkc.chatchannels.utility.Replace;
import com.clonkc.chatchannels.ChatChannels;
import com.clonkc.chatchannels.Configuration;
import com.clonkc.chatchannels.utility.SenderConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.CommandHook;

public class GlobalChatCommand {

    private final ChatChannels plugin;

    public GlobalChatCommand(ChatChannels plugin) {
        this.plugin = plugin;
    }

    @CommandHook("globalchat")
    public void onChat(CommandSender sender, String message) {
        Configuration config = plugin.getConfiguration();
        if (sender instanceof Player) {
            Bukkit.broadcastMessage(Replace.rep(config.getGlobalChatFormat(), "%player%", SenderConverter.convert(sender), "%message%", message));
        } else {
            Bukkit.getConsoleSender().sendMessage(Replace.rep(config.getGlobalChatFormat(), "%player%", SenderConverter.convert(sender), "%message%", message));
        }
    }
}
