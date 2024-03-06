package com.github.pietw3lve.flylimit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.pietw3lve.flylimit.FlyLimitPlugin;

import net.md_5.bungee.api.ChatColor;

public class Fly implements CommandExecutor {

    private final FlyLimitPlugin plugin;

    public Fly(FlyLimitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (plugin.getFlightPlayers().contains(player.getUniqueId())) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setFlySpeed(0.1f);
            player.sendMessage(ChatColor.RED + "Flight has been disabled.");
            plugin.getLogger().info(player.getName() + " flight has been disabled.");
            plugin.getFlightPlayers().remove(player.getUniqueId());
        } else {
            player.sendMessage(ChatColor.GREEN + "Flight has been enabled.");
            plugin.getLogger().info(player.getName() + " flight has been enabled.");
            plugin.getFlightPlayers().add(player.getUniqueId());
        }

        return true;
    }
}
