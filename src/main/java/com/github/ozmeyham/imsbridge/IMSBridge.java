package com.github.ozmeyham.imsbridge;

import com.github.ozmeyham.imsbridge.commands.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.checkBridgeKey;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;


public class IMSBridge {
	public static final Logger LOGGER = LoggerFactory.getLogger("imsbridge");
	public static Boolean bridgeEnabled = false;
	public static Boolean combinedBridgeEnabled = false;
	public static Boolean combinedBridgeChatEnabled = false;
	public static Integer delayTicks = 0;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		loadConfig();
		checkBridgeKey();
		ClientCommandHandler.instance.registerCommand(new BridgeKeyCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeColourCommand());
		ClientCommandHandler.instance.registerCommand(new BridgeHelpCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeChatCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeColourCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeHelpCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeMsgCommand());
		ClientCommandHandler.instance.registerCommand(new CombinedBridgeToggleCommand());
	}
}



//	public void onInitializeClient() {
//
//		loadConfig();
//		checkBridgeKey();
//
////		// Listen for outgoing cbridge chat messages
////		ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
////			if (combinedBridgeChatEnabled == true && wsClient != null && wsClient.isOpen() && bridgeKey != null) {
////				wsClient.send("{\"from\":\"mc\",\"msg\":\"" + message + "\",\"combinedbridge\":true}");
////				return false;
////			} else {
////				return true;
////			}
////		});
//		// Listen for outgoing guild messages
//		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
//			String content = message.getString();
//			if (content.contains("§2Guild >")) {
//				// Send to websocket
//				if (wsClient != null && wsClient.isOpen() && bridgeKey != null) {
//					wsClient.send("{\"from\":\"mc\",\"msg\":" + quote(content) + "}");
//				}
//			}
//		});
//
//		// Register "/bridgekey <key>" command to input bridge key
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeKeyCommand(dispatcher));
//		// Register "/bridge toggle" command to toggle receiving bridge messages
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeToggleCommand(dispatcher));
//		// Register "/bridge colour" command to format bridge messages.
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeColourCommand(dispatcher));
//		// Register "/bridge help" command to explain command usage.
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeHelpCommand(dispatcher));
//
//		// Register "/cbridge toggle" command to toggle receiving cbridge messages.
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeToggleCommand(dispatcher));
//		// Register "/cbridge chat" command to toggle cbridge chat functionality
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeChatCommand(dispatcher));
//		// Register "/bc <msg>" command to send messages to cbridge.
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeMsgCommand(dispatcher));
//		// Register "/cbridge colour" command to format cbridge messages.
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeColourCommand(dispatcher));
//		// Register "/cbridge help" command to explain cbridge command usage.
//		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeHelpCommand(dispatcher));
//	}
//}