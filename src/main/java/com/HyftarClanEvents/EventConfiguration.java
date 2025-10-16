package com.HyftarClanEvents;

public class EventConfiguration
{
    public String type;

    public int[] trackedItemIDs;

    public String webHook;

    public EventConfiguration(String type, String webHook, int[] trackedItemIDs)
    {
        this.type = type;
        this.webHook = webHook;
        this.trackedItemIDs = trackedItemIDs;
    }
}
