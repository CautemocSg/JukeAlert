package com.untamedears.JukeAlert.listener;

import static com.untamedears.JukeAlert.util.Utility.immuneToSnitch;
import static com.untamedears.JukeAlert.util.Utility.notifyGroup;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.untamedears.ItemExchange.events.IETransactionEvent;
import com.untamedears.JukeAlert.JukeAlert;
import com.untamedears.JukeAlert.manager.SnitchManager;
import com.untamedears.JukeAlert.model.Snitch;

public class ItemExchangeListener implements Listener {

	private final JukeAlert plugin = JukeAlert.getInstance();

	SnitchManager snitchManager = plugin.getSnitchManager();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void exchangeEvent(IETransactionEvent event) {

		Player player = event.getPlayer();
		Location location = event.getExchangeLocation();
		World world = location.getWorld();
		UUID accountId = player.getUniqueId();
		Set<Snitch> snitches = snitchManager.findSnitches(world, location);
		for (Snitch snitch : snitches) {
			if (!immuneToSnitch(snitch, accountId)) {
				snitch.imposeSnitchTax();
				if (snitch.shouldLog()) {
					try {
						TextComponent message = new TextComponent(ChatColor.LIGHT_PURPLE + " * "
								+ player.getDisplayName() + " at " + snitch.getName() + " exchanged "
								+ event.getInput()[1] + " " + event.getInput()[0] + " for "
								+ event.getOutput()[1] + " " + event.getOutput()[0] + " at "
								+ "[" + location.getX() + " " + location.getY() + " " + location.getZ() + "]");
						String hoverText = snitch.getHoverText(null, null);
						message.setHoverEvent(
								new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
						notifyGroup(snitch, message);
					} catch (SQLException | NullPointerException e) {
						JukeAlert.getInstance().getLogger().log(
								Level.SEVERE, "exchangeEvent generated an exception", e);
					}
					plugin.getJaLogger().logSnitchExchangeEvent(snitch, player, location);
				}
			}
		}
	}
}
