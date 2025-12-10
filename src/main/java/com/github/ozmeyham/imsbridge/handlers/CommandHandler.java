package com.github.ozmeyham.imsbridge.handlers;

import com.github.ozmeyham.imsbridge.commands.*;
import net.minecraftforge.client.ClientCommandHandler;

public class CommandHandler {
    public static void registerCommands() {
        ClientCommandHandler.instance.registerCommand(new CombinedBridgeCommands());
        ClientCommandHandler.instance.registerCommand(new BridgeCommands());
        ClientCommandHandler.instance.registerCommand(new BridgeOnlineAliasCommand());
        ClientCommandHandler.instance.registerCommand(new CombinedBridgeMessageCommand());
        ClientCommandHandler.instance.registerCommand(new CombinedBridgeToggleAliasCommand());
    }
}
