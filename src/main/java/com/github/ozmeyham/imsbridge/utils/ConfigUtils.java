package com.github.ozmeyham.imsbridge.utils;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

import static com.github.ozmeyham.imsbridge.IMSBridge.*;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;
import static com.github.ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static com.github.ozmeyham.imsbridge.commands.CombinedBridgeColourCommand.*;

public class ConfigUtils {
    private static Configuration config;

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        try {
            config.load();

            // Load settings
            bridgeKey = config.getString("bridgeKey", Configuration.CATEGORY_GENERAL, "", "Your bridge key");
            bridgeC1 = config.getString("bridge_colour1", Configuration.CATEGORY_GENERAL, "§9", "Bridge color 1");
            bridgeC2 = config.getString("bridge_colour2", Configuration.CATEGORY_GENERAL, "§6", "Bridge color 2");
            bridgeC3 = config.getString("bridge_colour3", Configuration.CATEGORY_GENERAL, "§f", "Bridge color 3");
            cbridgeC1 = config.getString("cbridge_colour1", Configuration.CATEGORY_GENERAL, "§4", "CBridge color 1");
            cbridgeC2 = config.getString("cbridge_colour2", Configuration.CATEGORY_GENERAL, "§6", "CBridge color 2");
            cbridgeC3 = config.getString("cbridge_colour3", Configuration.CATEGORY_GENERAL, "§f", "CBridge color 3");

            combinedBridgeEnabled = config.getBoolean("combinedBridgeEnabled", Configuration.CATEGORY_GENERAL, true, "Enable CBridge");
            bridgeEnabled = config.getBoolean("bridgeEnabled", Configuration.CATEGORY_GENERAL, true, "Enable Bridge");
            combinedBridgeChatEnabled = config.getBoolean("combinedBridgeChatEnabled", Configuration.CATEGORY_GENERAL, false, "Enable CBridge chat");

        }catch (Exception e) {
            logError("Error loading config", e); // Use IMSBridge.logError() instead of LOGGER.error()
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void saveConfig() {
        if (config != null) {
            config.save();
        }
    }

    public static void saveConfigValue(String key, String value) {
        if (config != null) {
            config.get(Configuration.CATEGORY_GENERAL, key, "").set(value);
            saveConfig();
        }
    }

    public static void saveConfigValue(String key, boolean value) {
        if (config != null) {
            config.get(Configuration.CATEGORY_GENERAL, key, true).set(value);
            saveConfig();
        }
    }
}