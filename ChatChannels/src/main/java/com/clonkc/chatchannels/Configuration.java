package com.clonkc.chatchannels;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;

@Getter
public class Configuration {
    private final ChatChannels plugin;
    private final YamlDocument config;

    private final String prefix;

    private final String noPermission;

    private final Float proximityRadius;
    private final String globalChatFormat;
    private final String localChatFormat;
    private final String selfFormat;
    private final String staffFormat;
    private final String staffChatKey;
    private final String lonely;

    private final String switchChannel;

    private final String consolePlayer;
    private final String reloadMsg;

    public Configuration(ChatChannels plugin, YamlDocument config) {
        this.plugin = plugin;
        this.config = config;

        prefix = config.getString("prefix");

        noPermission = config.getString("no-permission");

        proximityRadius = config.getFloat("proximity-radius");
        globalChatFormat = config.getString("global-chat-format");
        localChatFormat = config.getString("proximity-chat-format");
        selfFormat = config.getString("self-format");
        staffFormat = config.getString("staff-format");
        staffChatKey = config.getString("staff-chat-key");
        lonely = config.getString("lonely");

        switchChannel = config.getString("switch-channel");

        consolePlayer = config.getString("console-player");
        reloadMsg = config.getString("reload-message");
    }
}
