package com.github.pietw3lve.flylimit.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigManager {

    private FileConfiguration config;
    private float defaultFlightSpeed = 0.1f;
    private int defaultFlightTimer = 30;

    public ConfigManager(FileConfiguration config) {
        this.config = config;
    }

    public float getFlightSpeed(Player player) {
        ConfigurationSection ranksSection = config.getConfigurationSection("ranks");
        if (ranksSection != null) {
            int highestWeight = Integer.MIN_VALUE;
            float flightSpeed = defaultFlightSpeed;
            for (String rank : ranksSection.getKeys(false)) {
                if (player.hasPermission(ranksSection.getString(rank + ".permission"))) {
                    int weight = ranksSection.getInt(rank + ".weight");
                    if (weight > highestWeight) {
                        highestWeight = weight;
                        flightSpeed = (float) ranksSection.getDouble(rank + ".flight-speed");
                    }
                }
            }
            return flightSpeed;
        }
        return defaultFlightSpeed;
    }

    public int getFlightTimer(Player player) {
        ConfigurationSection ranksSection = config.getConfigurationSection("ranks");
        if (ranksSection != null) {
            int highestWeight = Integer.MIN_VALUE;
            int flightTimer = defaultFlightTimer;
            for (String rank : ranksSection.getKeys(false)) {
                if (player.hasPermission(ranksSection.getString(rank + ".permission"))) {
                    int weight = ranksSection.getInt(rank + ".weight");
                    if (weight > highestWeight) {
                        highestWeight = weight;
                        flightTimer = ranksSection.getInt(rank + ".flight-timer");
                    }
                }
            }
            return flightTimer;
        }
        return defaultFlightTimer;
    }
}
