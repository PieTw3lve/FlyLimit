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
        if (shouldEnableFlight(player)) {
            enableFlight(player);
        }
    }

    @EventHandler
    public void onPlayerFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (shouldStartFlightTimer(player)) {
            startFlightTimer(player);
        }
    }

    private boolean shouldEnableFlight(Player player) {
        UUID playerUuid = player.getUniqueId();
        if (plugin.isLandsIntegration()) {
            LandWorld world = plugin.getLandsApi().getWorld(player.getWorld());
            if (world != null && world.hasRoleFlag(playerUuid, player.getLocation(), Flags.FLY) &&
                    plugin.getFlightPlayers().contains(playerUuid) && player.isOnGround()) {
                return true;
            }
        } else {
            if (plugin.getFlightPlayers().contains(playerUuid) && player.isOnGround()) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldStartFlightTimer(Player player) {
        UUID playerUuid = player.getUniqueId();  
        if (plugin.isLandsIntegration()) {
            LandWorld world = plugin.getLandsApi().getWorld(player.getWorld());
            if (world != null && world.hasRoleFlag(playerUuid, player.getLocation(), Flags.FLY) &&
                    plugin.getFlightPlayers().contains(playerUuid) && !plugin.getFlightCountdownTaskIDs().containsKey(playerUuid)) {
                return true;
            }
        } else {
            if (plugin.getFlightPlayers().contains(playerUuid) && !plugin.getFlightCountdownTaskIDs().containsKey(playerUuid)) {
                return true;
            }
        }
        return false;
    }

    private void enableFlight(Player player) {
        UUID playerUuid = player.getUniqueId();
        player.setAllowFlight(true);
        player.setFlySpeed(plugin.getConfigManager().getFlightSpeed(player));
        cancelFlightTimer(playerUuid);
    }

    private void startFlightTimer(Player player) {
        UUID playerUuid = player.getUniqueId();
        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new FlightCountdown(plugin, player, plugin.getConfigManager().getFlightTimer(player)), 20L, 20L);
        plugin.getFlightCountdownTaskIDs().put(playerUuid, taskID);
    }

    private void cancelFlightTimer(UUID playerUuid) {
        if (plugin.getFlightCountdownTaskIDs().containsKey(playerUuid)) {
            Bukkit.getScheduler().cancelTask(plugin.getFlightCountdownTaskIDs().remove(playerUuid));
        }
    }
}
