package com.HyftarClanEvents;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("hyftar-clan-events")
public interface HyftarClanEventsConfig extends Config
{
	@ConfigItem(
		keyName = "event_config",
		name = "Event configuration",
		description = "Paste your event's configuration here (copied from the website)"
	)
	default String eventConfiguration()
	{
		return "";
	}
}
