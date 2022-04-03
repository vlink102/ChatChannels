package com.clonkc.chatchannels.listeners;

import com.clonkc.chatchannels.ChatChannels;
import com.clonkc.chatchannels.Configuration;
import com.clonkc.chatchannels.utility.MathUtils;
import com.clonkc.chatchannels.utility.Replace;
import com.clonkc.chatchannels.utility.SenderConverter;
import com.sun.tools.javac.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.CommandHook;

import java.util.ArrayList;
import java.util.List;

public class LocalChatCommand {

    private final ChatChannels plugin;

    public LocalChatCommand(ChatChannels plugin) {
        this.plugin = plugin;
    }

    @CommandHook("localchat")
    public void onLocalChat(CommandSender sender, String message) {
        Configuration configuration = plugin.getConfiguration();
        if (sender instanceof Player) {
            int receiverCount = 0;
            List<Player> players = new ArrayList<>();

            for (Entity entity : ((Player) sender).getNearbyEntities(configuration.getProximityRadius(), configuration.getProximityRadius(), configuration.getProximityRadius())) {
                if (entity instanceof Player player) {
                    double dist = MathUtils.calculateDistance3D(((Player) sender).getLocation(), player.getLocation());
                    if (dist <= configuration.getProximityRadius()) {
                        if (player.hasPermission("chatchannels.see.localchat")) {
                            receiverCount += 1;
                            players.add(player);
                        }
                    }
                }
            }

            if (receiverCount == 0) {
                sender.sendMessage(Replace.rep(configuration.getLonely()));
            } else {
                sender.sendMessage(Replace.rep(configuration.getSelfFormat(),
                        "%player%", SenderConverter.convert(sender),
                        "%message%", message,
                        "%receivercount%", String.valueOf(receiverCount)));
                for (Player player : players) {
                    double dist = MathUtils.calculateDistance3D(player.getLocation(), ((Player) sender).getLocation());
                    String truncatedDist = String.valueOf(MathUtils.round(dist, 2));
                    if (dist <= configuration.getProximityRadius()) {
                        player.sendMessage(Replace.rep(configuration.getLocalChatFormat(),
                                "%player%", SenderConverter.convert(sender),
                                "%message%", message,
                                "%blocks%", truncatedDist));
                    }
                }
            }
        } else {
            sender.sendMessage(configuration.getNoPermission());
        }

    }
}
