// src/main/java/com/github/ozmeyham/imsbridge/IMSBridge.java

package com.github.ozmeyham.imsbridge;

import com.github.ozmeyham.imsbridge.handlers.ChatGuiOpenHandler;  // ← new
import com.github.ozmeyham.imsbridge.utils.GuildChatHandler;
import com.github.ozmeyham.imsbridge.commands.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.checkBridgeKey;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

@Mod(modid = "imsbridge", useMetadata = true)
public class IMSBridge {
	// toggles
	public static boolean bridgeEnabled           = false;
	public static boolean combinedBridgeEnabled   = false;
	public static boolean combinedBridgeChatEnabled = false;
	public static int     delayTicks              = 0;

	// ← these six get filled from config/imsbridge.cfg at startup:
	public static String bridgeC1, bridgeC2, bridgeC3;
	public static String cbridgeC1, cbridgeC2, cbridgeC3;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// listen for in-game guild chat:
		MinecraftForge.EVENT_BUS.register(new GuildChatHandler());

		// ← NEW: when the vanilla chat GUI opens, swap in your PrefixingChatGui
		MinecraftForge.EVENT_BUS.register(new ChatGuiOpenHandler());

		printToChat("Loading configuration...");
		loadConfig();          // ← reads config file and populates bridgeC1…cbridgeC3

		printToChat("Checking Bridge Key...");
		checkBridgeKey();      // ← kicks off your websocket if the key is valid

		// register all of your /bridge* and /cbridge* commands:
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeToggleAliasCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeChatAliasCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeMessageAliasCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeToggleAliasCommand());
	}
}
