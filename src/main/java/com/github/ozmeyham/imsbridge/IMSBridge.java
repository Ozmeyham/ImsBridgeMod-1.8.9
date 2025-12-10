// src/main/java/com/github/ozmeyham/imsbridge/IMSBridge.java

package com.github.ozmeyham.imsbridge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static com.github.ozmeyham.imsbridge.handlers.CommandHandler.registerCommands;
import static com.github.ozmeyham.imsbridge.handlers.EventHandler.eventListener;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;

@Mod(modid = "imsbridge", useMetadata = true)
public class IMSBridge {
	// toggles
	public static boolean bridgeEnabled           = false;
	public static boolean combinedBridgeEnabled   = false;
	public static boolean combinedBridgeChatEnabled = false;
	public static boolean firstLogin = true;
	public static boolean checkedForUpdate = false;

	// these get filled from config/imsbridge.cfg at startup:
	public static String bridgeC1, bridgeC2, bridgeC3;
	public static String cbridgeC1, cbridgeC2, cbridgeC3;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		loadConfig();           // reads/populates config file
		registerCommands();
		eventListener();
	}
}
