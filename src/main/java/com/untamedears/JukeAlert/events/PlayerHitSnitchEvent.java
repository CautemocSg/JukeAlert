package com.untamedears.JukeAlert.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.untamedears.JukeAlert.model.Snitch;

/**
 * Called when a player hits a snitch, meaning he enters the snitch range
 * without having perms to do so without triggering a notification
 *
 */
public class PlayerHitSnitchEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private Snitch snitch;

	public PlayerHitSnitchEvent(Snitch snitch, Player player) {
		super(player);
		this.snitch = snitch;
	}

	public Snitch getSnitch() {
		return snitch;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
