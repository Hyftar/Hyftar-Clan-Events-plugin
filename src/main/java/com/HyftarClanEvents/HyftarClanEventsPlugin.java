package com.HyftarClanEvents;

import com.google.gson.JsonSyntaxException;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.loottracker.LootReceived;
import net.runelite.http.api.loottracker.LootRecordType;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
	name = "Hyftar's Clan Events",
	description = "Plugin to track loot and kill times for clan events"
)
public class HyftarClanEventsPlugin extends Plugin
{
	final static String CONFIG_GROUP = "hyftar-clan-events";
	final static String EVENT_CONFIG_KEY = "event_config";

	private EventConfiguration eventConfiguration;

	@Inject
	private Client client;

	@Inject
	private HyftarClanEventsConfig config;

	@Override
	protected void startUp() throws Exception
	{
		this.setEventConfiguration(config.eventConfiguration());
	}

	@Subscribe
	protected void onLootReceived(final LootReceived lootReceived)
	{
		if (this.eventConfiguration == null
			|| this.eventConfiguration.trackedItemIDs == null)
		{
			return;
		}

		if (lootReceived.getType() != LootRecordType.NPC
			&& lootReceived.getType() != LootRecordType.EVENT)
		{
			return;
		}

		List<Integer> trackedItemIds =
			lootReceived
				.getItems()
				.stream()
				.map(ItemStack::getId)
				.filter(
					i ->
						Arrays.stream(this.eventConfiguration.trackedItemIDs)
							.anyMatch(trackedId -> trackedId == i)
				)
				.collect(Collectors.toList());

		if (trackedItemIds.size() < 1)
		{
			return;
		}

		this.submitLootWithData(lootReceived);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(CONFIG_GROUP)) {
			return;
		}

		if (EVENT_CONFIG_KEY.equals(event.getKey())) {
			setEventConfiguration(event.getNewValue());
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
	}

	private void setEventConfiguration(String value)
	{
		try
		{
			Gson gson = new Gson();
			this.eventConfiguration = gson.fromJson(value, EventConfiguration.class);
		}
		catch (JsonSyntaxException exception)
		{
			log.error(String.format("[Hyftar's Clan Events] -> Invalid JSON syntax : %s", value));
		}
	}

	private void submitLootWithData(LootReceived lootReceived)
	{
		if (this.eventConfiguration.webHook == null)
		{
			return;
		}

		try
		{
			// TODO : Send to webhook
		}
		catch (Exception e)
		{
			log.error(
				String.format(
					"[Hyftar's Clan Events] -> Failed sending to send to webhook : %s",
					this.eventConfiguration.webHook
				)
			);
		}
	}

	@Provides
	HyftarClanEventsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HyftarClanEventsConfig.class);
	}
}
