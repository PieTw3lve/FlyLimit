package com.github.pietw3lve.flylimit.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.github.pietw3lve.flylimit.FlyLimitPlugin;
import com.github.pietw3lve.flylimit.utils.FlightCountdown;

import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.land.LandWorld;

public class PlayerListener implements Listener {

    private final FlyLimitPlugin plugin;

    public PlayerListener(FlyLimitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        LandWorld world = plugin.getLandsApi().getWorld(player.getWorld());
        if (world != null) {
            if (world.hasRoleFlag(playerUuid, player.getLocation(), Flags.FLY) && plugin.getFlightPlayers().contains(playerUuid) && player.isOnGround()) {
                enableFlight(player);
            }
        }
    }

    @EventHandler
    public void onPlayerFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        LandWorld world = plugin.getLandsApi().getWorld(player.getWorld());
        if (world != null) {
            if (world.hasRoleFlag(playerUuid, player.getLocation(), Flags.FLY) && plugin.getFlightPlayers().contains(playerUuid) && !plugin.getFlightCountdownTaskIDs().containsKey(playerUuid)) {
                int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new FlightCountdown(plugin, player, plugin.getConfigManager().getFlightTimer(player)), 20L, 20L);
                plugin.getFlightCountdownTaskIDs().put(playerUuid, taskID);
            }
        }
    }

    public void enableFlight(Player player) {
        UUID playerUuid = player.getUniqueId();
        player.setAllowFlight(true);
        player.setFlySpeed(plugin.getConfigManager().getFlightSpeed(player));
        if (plugin.getFlightCountdownTaskIDs().containsKey(playerUuid)) {
            Bukkit.getScheduler().cancelTask(plugin.getFlightCountdownTaskIDs().remove(playerUuid));
            plugin.getFlightCountdownTaskIDs().remove(playerUuid);
        }
    }
}
