package com.clonkc.chatchannels.listeners;

import com.clonkc.chatchannels.Channels;
import com.clonkc.chatchannels.ChatChannels;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerConnection implements Listener {

    private final ChatChannels plugin;

    public PlayerConnection(ChatChannels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getPersistentDataContainer().isEmpty()) {
            PersistentDataContainer container = e.getPlayer().getPersistentDataContainer();
            NamespacedKey key = plugin.getKey();
            container.set(key, PersistentDataType.STRING, Channels.LOCAL.getAbilityName());
        }
    }
}
