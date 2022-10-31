package com.clonkc.chatchannels.listeners;

import com.clonkc.chatchannels.Channels;
import com.clonkc.chatchannels.ChatChannels;
import com.clonkc.chatchannels.Configuration;
import com.clonkc.chatchannels.utility.*;
import com.sun.tools.javac.Main;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.Pattern;
import dev.dejvokep.boostedyaml.dvs.segment.Segment;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChatEventListener implements Listener {

    private final ChatChannels plugin;

    public ChatEventListener(ChatChannels plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Configuration configuration = plugin.getConfiguration();

        NamespacedKey key = plugin.getKey();

        if (e.getMessage().startsWith(configuration.getStaffChatKey())) {
            if (e.getPlayer().hasPermission("chatchannels.switch.staff")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("chatchannels.see.staffchat")) {
                        player.sendMessage(Replace.rep(configuration.getStaffFormat(), "%player%", e.getPlayer().getDisplayName(), "%message%", e.getMessage(), "%world%", e.getPlayer().getWorld().getName(), "%coordinates%", LocationUtils.convert(e.getPlayer().getLocation())));
                    }
                }
            } else {
                e.getPlayer().sendMessage(configuration.getNoPermission());
            }
        } else {
            if (Objects.requireNonNull(e.getPlayer().getPersistentDataContainer().get(key, PersistentDataType.STRING)).equalsIgnoreCase(Channels.LOCAL.getAbilityName())) {
                int receiverCount = 0;
                List<Player> players = new ArrayList<>();

                for (Player player : e.getRecipients()) {
                    if (!player.equals(e.getPlayer())) {
                        double dist = MathUtils.calculateDistance3D(player.getLocation(), e.getPlayer().getLocation());
                        if (dist <= configuration.getProximityRadius()) {
                            if (player.hasPermission("chatchannels.see.localchat")) {
                                receiverCount += 1;
                                players.add(player);
                            }
                        }
                    }
                }

                if (receiverCount == 0) {
                    e.getPlayer().sendMessage(Replace.rep(configuration.getLonely()));
                } else {
                    e.getPlayer().sendMessage(Replace.rep(configuration.getSelfFormat(),
                            "%player%", e.getPlayer().getDisplayName(),
                            "%message%", e.getMessage(),
                            "%receivercount%", String.valueOf(receiverCount)));
                    for (Player player : players) {
                        double dist = MathUtils.calculateDistance3D(player.getLocation(), e.getPlayer().getLocation());
                        String truncatedDist = String.valueOf(MathUtils.round(dist, 2));
                        if (dist <= configuration.getProximityRadius()) {
                            player.sendMessage(Replace.rep(configuration.getLocalChatFormat(),
                                    "%player%", e.getPlayer().getDisplayName(),
                                    "%message%", e.getMessage(),
                                    "%blocks%", truncatedDist));
                        }
                    }
                }
            } else if (Objects.requireNonNull(e.getPlayer().getPersistentDataContainer().get(key, PersistentDataType.STRING)).equalsIgnoreCase(Channels.GLOBAL.getAbilityName())) {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Replace.rep(configuration.getGlobalChatFormat(), "%player%", e.getPlayer().getDisplayName(), "%message%", e.getMessage())));
            } else if (Objects.requireNonNull(e.getPlayer().getPersistentDataContainer().get(key, PersistentDataType.STRING)).equalsIgnoreCase(Channels.STAFF.getAbilityName())) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("chatchannels.see.staffchat")) {
                        player.sendMessage(Replace.rep(configuration.getStaffFormat(), "%player%", player.getDisplayName(), "%message%", e.getMessage(), "%world%", e.getPlayer().getWorld().getName(), "%coordinates%", LocationUtils.convert(e.getPlayer().getLocation())));
                    }
                }
            } else {
                try {
                    YamlDocument customChannels = YamlDocument.create(new File(plugin.getDataFolder(), "customchannels.yml"), Objects.requireNonNull(plugin.getResource("customchannels.yml")),
                            GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build());
                    List<String> keyList = new ArrayList<>();
                    for (Object keys : customChannels.getKeys()) {
                        keyList.add(keys.toString());
                    }
                    if (keyList.contains(e.getPlayer().getPersistentDataContainer().get(key, PersistentDataType.STRING))) {
                        String k = e.getPlayer().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission("chatchannels.see." + e.getPlayer().getPersistentDataContainer().get(key, PersistentDataType.STRING))) {
                                player.sendMessage(Replace.rep(customChannels.getString(k + ".format"), "%player%", e.getPlayer().getDisplayName(), "%message%", e.getMessage(), "%coordinates%", LocationUtils.convert(e.getPlayer().getLocation()), "%world%", e.getPlayer().getWorld().getName(), "%blocks%", String.valueOf(MathUtils.round(MathUtils.calculateDistance3D(e.getPlayer().getLocation(), player.getLocation()), 2))));
                            }
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
