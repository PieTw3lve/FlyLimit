package com.github.pietw3lve.flylimit;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.pietw3lve.flylimit.commands.Fly;
import com.github.pietw3lve.flylimit.commands.Reload;
import com.github.pietw3lve.flylimit.listeners.PlayerListener;
import com.github.pietw3lve.flylimit.utils.ConfigManager;

import me.angeschossen.lands.api.LandsIntegration;

public class FlyLimitPlugin extends JavaPlugin {

    private LandsIntegration landsApi;
    private Set<UUID> flightPlayers;
    private Map<UUID, Integer> flightCountdownTaskIDs;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        landsApi = LandsIntegration.of(this);
    
        saveDefaultConfig();
        flightPlayers = new HashSet<>();
        flightCountdownTaskIDs = new HashMap<>();
        configManager = new ConfigManager(getConfig());

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("fly").setExecutor(new Fly(this));
        getCommand("flreload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        for (int taskId : flightCountdownTaskIDs.values()) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        flightPlayers.clear();
        flightCountdownTaskIDs.clear();
    }

    public LandsIntegration getLandsApi() {
        return landsApi;
    }

    public void setLandsApi(LandsIntegration landsApi) {
        this.landsApi = landsApi;
    }

    public Set<UUID> getFlightPlayers() {
        return flightPlayers;
    }

    public void setFlightPlayers(Set<UUID> flightPlayers) {
        this.flightPlayers = flightPlayers;
    }

    public Map<UUID, Integer> getFlightCountdownTaskIDs() {
        return flightCountdownTaskIDs;
    }

    public void setFlightCountdownTaskIDs(Map<UUID, Integer> flightCountdownTaskIDs) {
        this.flightCountdownTaskIDs = flightCountdownTaskIDs;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}