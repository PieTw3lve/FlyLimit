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
    private boolean landsIntegration;
    private Set<UUID> flightPlayers = new HashSet<>();
    private Map<UUID, Integer> flightCountdownTaskIDs = new HashMap<>();
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Integrations
        initializeLandsIntegration();
        // Config
        saveDefaultConfig();
        setConfigManager(new ConfigManager(getConfig()));
        // Events
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        // Commands
        getCommand("fly").setExecutor(new Fly(this));
        getCommand("flreload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        cancelFlightTasks();
    }

    private void initializeLandsIntegration() {
        try {
            landsApi = LandsIntegration.of(this);
            landsIntegration = true;
            this.getLogger().info("Lands plugin was found.");
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
            landsIntegration = false;
            this.getLogger().info("Lands plugin is not found or incompatible.");
        }
    }

    private void cancelFlightTasks() {
        for (int taskId : flightCountdownTaskIDs.values()) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        flightPlayers.clear();
        flightCountdownTaskIDs.clear();
    }

    public LandsIntegration getLandsApi() {
        return landsApi;
    }

    public boolean isLandsIntegration() {
        return landsIntegration;
    }

    public boolean getLandsIntegration() {
        return this.landsIntegration;
    }

    public void setLandsIntegration(boolean landsIntegration) {
        this.landsIntegration = landsIntegration;
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

    public void setFlightCountdownTaskIDs(Map<UUID,Integer> flightCountdownTaskIDs) {
        this.flightCountdownTaskIDs = flightCountdownTaskIDs;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void setLandsApi(LandsIntegration landsApi) {
        this.landsApi = landsApi;
    }
}
