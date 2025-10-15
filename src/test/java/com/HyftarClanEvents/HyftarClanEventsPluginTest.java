package com.HyftarClanEvents;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HyftarClanEventsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HyftarClanEventsPlugin.class);
		RuneLite.main(args);
	}
}