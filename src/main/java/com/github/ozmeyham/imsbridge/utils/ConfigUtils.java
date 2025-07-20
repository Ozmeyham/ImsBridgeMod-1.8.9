// src/main/java/com/github/ozmeyham/imsbridge/utils/ConfigUtils.java

package com.github.ozmeyham.imsbridge.utils;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.io.File;
import java.nio.file.Path;

import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;
import static com.github.ozmeyham.imsbridge.IMSBridge.*;

public class ConfigUtils {
    public static final String CONFIG_FILE_NAME = "imsbridge.cfg";

    public static Path getConfigPath() {
        return FMLClientHandler
                .instance()
                .getClient()
                .mcDataDir
                .toPath()
                .resolve("config")
                .resolve(CONFIG_FILE_NAME);
    }

    public static void loadConfig() {
        File configFile = getConfigPath().toFile();
        Configuration cfg = new Configuration(configFile);
        cfg.load();

        // your API key lives in BridgeKeyUtils.bridgeKey
        bridgeKey = cfg.getString(
                "bridgeKey",
                Configuration.CATEGORY_GENERAL,
                "",
                "IMSBridge API key"
        );

        // load all six colour fields into the static IMSBridge.* variables
        bridgeC1  = cfg.getString("bridge_colour1",  Configuration.CATEGORY_GENERAL, "§9", "Bridge colour 1");
        bridgeC2  = cfg.getString("bridge_colour2",  Configuration.CATEGORY_GENERAL, "§6", "Bridge colour 2");
        bridgeC3  = cfg.getString("bridge_colour3",  Configuration.CATEGORY_GENERAL, "§f", "Bridge colour 3");
        cbridgeC1 = cfg.getString("cbridge_colour1", Configuration.CATEGORY_GENERAL, "§4", "Combined bridge colour 1");
        cbridgeC2 = cfg.getString("cbridge_colour2", Configuration.CATEGORY_GENERAL, "§6", "Combined bridge colour 2");
        cbridgeC3 = cfg.getString("cbridge_colour3", Configuration.CATEGORY_GENERAL, "§f", "Combined bridge colour 3");

        // load your toggles, too (if you want to drive them from config)
        combinedBridgeEnabled    = cfg.getBoolean(
                "combinedBridgeEnabled",
                Configuration.CATEGORY_GENERAL,
                false,
                "Enable combined bridge chat"
        );
        bridgeEnabled            = cfg.getBoolean(
                "bridgeEnabled",
                Configuration.CATEGORY_GENERAL,
                true,
                "Enable Minecraft→Discord bridge"
        );
        combinedBridgeChatEnabled = cfg.getBoolean(
                "combinedBridgeChatEnabled",
                Configuration.CATEGORY_GENERAL,
                false,
                "Enable combined chat mode"
        );

        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    public static void saveConfigValue(String key, String value) {
        File configFile = getConfigPath().toFile();
        Configuration cfg = new Configuration(configFile);
        cfg.load();
        cfg.get(Configuration.CATEGORY_GENERAL, key, value).set(value);
        cfg.save();
    }
}
