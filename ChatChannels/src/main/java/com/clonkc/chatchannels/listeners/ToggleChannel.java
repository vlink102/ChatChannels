package com.clonkc.chatchannels.listeners;

import com.clonkc.chatchannels.Channels;
import com.clonkc.chatchannels.utility.Replace;
import com.clonkc.chatchannels.ChatChannels;
import com.clonkc.chatchannels.Configuration;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.Pattern;
import dev.dejvokep.boostedyaml.dvs.segment.Segment;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.commandmanager.CommandHook;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ToggleChannel {

    private final ChatChannels plugin;

    public ToggleChannel(ChatChannels plugin) {
        this.plugin = plugin;
    }

    @CommandHook("togglechannel")
    public void toggle(CommandSender sender, Channels channel) {
        Configuration config = plugin.getConfiguration();

        if (sender instanceof Player player) {
            PersistentDataContainer container = player.getPersistentDataContainer();
            NamespacedKey key = plugin.getKey();
            if (player.hasPermission("chatchannels.switch." + channel.getAbilityName())) {
                container.set(key, PersistentDataType.STRING, channel.getAbilityName());
                player.sendMessage(Replace.rep(config.getPrefix() + config.getSwitchChannel(), "%channel%", channel.getAbilityName()));
            } else {
                player.sendMessage(Replace.rep(config.getNoPermission()));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(Replace.rep("&cYou cannot toggle between channels as the console!"));
        }
    }

    @CommandHook("togglechannelstring")
    public void toggleString(CommandSender sender, String channel) {
        Configuration configuration = plugin.getConfiguration();
        try {
            YamlDocument customChannels = YamlDocument.create(new File(plugin.getDataFolder(), "customchannels.yml"), Objects.requireNonNull(plugin.getResource("customchannels.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build());
            if (customChannels.getOptionalSection(channel).isPresent()) {
                if (customChannels.getOptionalString(channel + ".format").isPresent()) {
                    if (sender instanceof Player player) {
                        PersistentDataContainer container = player.getPersistentDataContainer();
                        NamespacedKey key = plugin.getKey();
                        if (player.hasPermission("chatchannels.switch." + channel)) {
                            container.set(key, PersistentDataType.STRING, channel);
                            player.sendMessage(Replace.rep(configuration.getPrefix() + configuration.getSwitchChannel(), "%channel%", channel));
                        } else {
                            player.sendMessage(Replace.rep(configuration.getNoPermission()));
                        }
                    } else {
                        Bukkit.getConsoleSender().sendMessage(Replace.rep("&cYou cannot toggle between channels as the console!"));
                    }
                } else {
                    sender.sendMessage(Replace.rep("&c" + channel + "&c has malformed or invalid format. Please check the customchannels.yml"));
                }
            } else {
                sender.sendMessage(Replace.rep("&cThat channel does not exist!"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
