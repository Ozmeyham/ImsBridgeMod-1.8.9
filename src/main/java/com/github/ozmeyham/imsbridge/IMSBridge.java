package com.github.ozmeyham.imsbridge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import com.github.ozmeyham.imsbridge.commands.*;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

@Mod(modid = IMSBridge.MODID, name = IMSBridge.NAME, version = IMSBridge.VERSION, clientSideOnly = true)
public class IMSBridge {
	public static final String MODID = "imsbridge";
	public static final String NAME = "IMS Bridge";
	public static final String VERSION = "1.0";

	public static Logger logger;

	// Configuration options
	public static Boolean bridgeEnabled = false;
	public static Boolean combinedBridgeEnabled = false;
	public static Boolean combinedBridgeChatEnabled = false;

	// Simple logging method
	public static void log(String message) {
		FMLLog.log(MODID, Level.INFO, "[IMSBridge] %s", message);
	}

	// Error logging method
	public static void logError(String message, Throwable ex) {
		FMLLog.log(MODID, Level.ERROR, ex, "[IMSBridge] %s", message);
	}
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		ConfigUtils.loadConfig(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register commands
		ClientCommandHandler.instance.registerCommand(new BridgeCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeColourCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeHelpCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeKeyCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeChatCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeColourCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeHelpCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeMsgCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeToggleCommand());

		// Register event handlers
		MinecraftForge.EVENT_BUS.register(new BridgeKeyUtils());
	}
}