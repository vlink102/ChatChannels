package com.clonkc.chatchannels;

public enum Channels {
    GLOBAL("global"),
    G("global"),
    A("global"),

    LOCAL("local"),
    L("local"),
    PROXIMITY("local"),
    NEARBY("local"),
    NEAR("local"),

    STAFF("staff"),
    S("staff");

    private final String channelName;

    Channels(String channelName) {
        this.channelName = channelName;
    }

    public String getAbilityName() {
        return channelName;
    }
}
