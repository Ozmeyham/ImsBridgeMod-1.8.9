package com.github.ozmeyham.imsbridge;

import net.minecraft.util.ChatComponentText;
import com.github.ozmeyham.imsbridge.commands.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import com.github.ozmeyham.imsbridge.utils.GuildChatHandler;

import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.checkBridgeKey;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.connectWebSocket;

@Mod(modid = "imsbridge", useMetadata = true)
public class IMSBridge {
	public static Boolean bridgeEnabled = false;
	public static Boolean combinedBridgeEnabled = false;
	public static Boolean combinedBridgeChatEnabled = false;
	public static Integer delayTicks = 0;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// 1) register your chat handler...
		MinecraftForge.EVENT_BUS.register(new GuildChatHandler());

		printToChat("Loading configuration...");
		loadConfig();

		printToChat("Checking Bridge Key...");
		checkBridgeKey();

		// 2) register all your client commands
		ClientCommandHandler.instance.registerCommand(new BridgeKeyCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeColourCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeHelpCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeChatCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeColourCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeHelpCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeMsgCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeToggleCommand());

		// 3) open the WebSocket _once_ on startup
		connectWebSocket();
	}
}
