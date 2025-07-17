package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;


import static ozmeyham.imsbridge.IMSBridge.combinedBridgeChatEnabled;

import static ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeChatCommand {
    public static void combinedBridgeChatCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("chat")
                        .executes(ctx -> {
                            if (combinedBridgeChatEnabled == false) {
                                if (combinedBridgeEnabled) {
                                    combinedBridgeChatEnabled = true;
                                    saveConfigValue("combinedBridgeChatEnabled", "true");
                                    printToChat("§aEnabled combined bridge chat! §e§iThis functions like /chat guild, but for cbridge.");
                                } else {
                                    printToChat("§cYou need to enable cbridge messages before using this command! Do /cbridge toggle");
                                }
                            } else {
                                combinedBridgeChatEnabled = false;
                                saveConfigValue("combinedBridgeChatEnabled", "false");
                                printToChat("§cDisabled Combined Bridge Chat!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
