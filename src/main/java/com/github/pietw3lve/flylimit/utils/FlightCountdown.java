package com.github.pietw3lve.flylimit.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.github.pietw3lve.flylimit.FlyLimitPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class FlightCountdown implements Runnable {

    private final FlyLimitPlugin plugin;
    private final Player player;
    private int totalTime;
    private int timeLeft;

    public FlightCountdown(FlyLimitPlugin plugin, Player player, int totalTime) {
        this.plugin = plugin;
        this.player = player;
        this.totalTime = totalTime;
        this.timeLeft = totalTime;
    }

    @Override
    public void run() {
        if (player.isFlying()) {
            timeLeft--;
            if (timeLeft <= 0) {
                player.setFlying(false);
                player.setAllowFlight(false);
                Bukkit.getScheduler().cancelTask(plugin.getFlightCountdownTaskIDs().remove(player.getUniqueId()));
            } else if (timeLeft <= totalTime * 0.1) {
                playWarningSound();
            }
            spawnFlyParticles();
            updateActionBar();
        }
    }

    private void spawnFlyParticles() {
        player.spawnParticle(Particle.CLOUD, player.getLocation(), 5, 0.25, 0.25, 0.25, 0.05);
    }

    private void playWarningSound() {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
    }

    private void updateActionBar() {
        double ratio = (double) timeLeft / totalTime;

        ChatColor timerColor;
        if (ratio < 0.3) {
            timerColor = ChatColor.RED;
        } else if (ratio < 0.6) {
            timerColor = ChatColor.YELLOW;
        } else {
            timerColor = ChatColor.GREEN;
        }

        String message = ChatColor.GRAY + "Flight Time: " + timerColor + timeLeft + "s";
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
