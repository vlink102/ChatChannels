package com.clonkc.chatchannels;

import com.clonkc.chatchannels.listeners.*;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.Pattern;
import dev.dejvokep.boostedyaml.dvs.segment.Segment;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.ArgType;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.commandmanager.Constraint;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public final class ChatChannels extends JavaPlugin {

    static ChatChannels instance;

    public static ChatChannels getInstance() {
        return instance;
    }

    private final NamespacedKey key = new NamespacedKey(this, "chatchannel");

    public NamespacedKey getKey() {
        return key;
    }

    @Getter
    @Setter
    private Configuration configuration;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getLogger().log(Level.INFO,"&9&m----------&r &bChat Channels&r &9&m----------");
        try {
            registerListeners();
            Bukkit.getLogger().log(Level.INFO, "&0 > &6Listeners registered");
            registerCommands();
            Bukkit.getLogger().log(Level.INFO, "&0 > &6Commands registered");
            createConfigFiles();
            Bukkit.getLogger().log(Level.INFO, "&0 > &6Config Files initialized");
            Bukkit.getLogger().log(Level.INFO, "&0 > &6Setup Complete!");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "ChatChannels failed to startup, please read the stacktrace below (Plugin is now disabled)");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getLogger().log(Level.INFO, "&9&m----------------------------------");

    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatEventListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConnection(this), this);
    }

    public void registerCommands() {
        ArgType<World> worldType = new ArgType<>("world", s -> Bukkit.getWorld(s))
                .tabStream(c -> Bukkit.getWorlds().stream().map(World::getName))
                .constraint(s -> {
                    World.Environment env = World.Environment.valueOf(s.toUpperCase());
                    return Constraint.of(ChatColor.RED + "World must be of type " + s, (sender, world) -> world.getEnvironment() == env);
                });

        ArgType<Channels> channelType = new ArgType<>("channel", s -> Channels.valueOf(s.toUpperCase()))
                .tabStream(c -> Arrays.stream(Channels.values())
                        .map(Channels::getAbilityName).map(String::toLowerCase));

        ArgType<Material> materialType = ArgType.of("material", Material.class);

        GlobalChatCommand globalChatCommand = new GlobalChatCommand(this);
        ReloadCommand reloadCommand = new ReloadCommand(this);
        ToggleChannel toggleChannel = new ToggleChannel(this);
        LocalChatCommand localChatCommand = new LocalChatCommand(this);
        StaffChatCommand staffChatCommand = new StaffChatCommand(this);

        new CommandParser(this.getResource("commands.rdcml"))
                .setArgTypes(worldType, materialType, channelType)
                .parse().register("vlu", globalChatCommand, reloadCommand, toggleChannel, staffChatCommand, localChatCommand);
    }

    public void reloadConfigFiles() {
        try {
            YamlDocument config = YamlDocument.create(new File(getDataFolder(), "config.yml"), Objects.requireNonNull(getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build());
            YamlDocument customChannels = YamlDocument.create(new File(getDataFolder(), "customchannels.yml"), Objects.requireNonNull(getResource("customchannels.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build());
            config.update();
            customChannels.update();
            config.reload();
            customChannels.reload();
            config.save();
            customChannels.save();
            setConfiguration(new Configuration(this, config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createConfigFiles() {
        try {
            setConfiguration(new Configuration(this, YamlDocument.create(new File(getDataFolder(), "config.yml"), Objects.requireNonNull(getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build())));
            YamlDocument.create(new File(getDataFolder(), "customchannels.yml"), Objects.requireNonNull(getResource("customchannels.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setAutoSave(true).setVersioning(new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10)), "file-version").build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
