package com.clonkc.chatchannels.listeners;

import com.clonkc.chatchannels.utility.Replace;
import com.clonkc.chatchannels.ChatChannels;
import com.clonkc.chatchannels.Configuration;
import org.bukkit.command.CommandSender;
import redempt.redlib.commandmanager.CommandHook;

public class ReloadCommand {
    private final ChatChannels plugin;

    public ReloadCommand(ChatChannels plugin) {
        this.plugin = plugin;
    }

    @CommandHook("reload")
    public void onReload(CommandSender sender) {
        Configuration config = plugin.getConfiguration();
        plugin.reloadConfigFiles();
        sender.sendMessage(Replace.rep(config.getPrefix() + config.getReloadMsg()));
    }
}
