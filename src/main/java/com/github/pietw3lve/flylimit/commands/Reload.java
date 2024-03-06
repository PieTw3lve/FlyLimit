package com.github.pietw3lve.flylimit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.pietw3lve.flylimit.FlyLimitPlugin;
import com.github.pietw3lve.flylimit.utils.ConfigManager;

import net.md_5.bungee.api.ChatColor;

public class Reload implements CommandExecutor {
    
    private final FlyLimitPlugin plugin;

    public Reload(FlyLimitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        plugin.setConfigManager(new ConfigManager(plugin.getConfig()));
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully.");
        plugin.getLogger().info(sender.getName() + " has reloaded the configurations.");
        return true;
    }
}
