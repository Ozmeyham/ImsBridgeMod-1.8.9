package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.connectWebSocket;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.*;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;

public class BridgeKeyCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bridgekey";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridgekey <key> - Set your bridge key";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            bridgeKey = args[0];
            if (isValidBridgeKey()) {
                saveConfigValue("bridgeKey", bridgeKey);
                sender.addChatMessage(new ChatComponentText("§cBridge key saved as: §f" + bridgeKey));
                connectWebSocket("wss.ims-bridge.com");
            } else {
                sender.addChatMessage(new ChatComponentText("§cInvalid bridge key format!"));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}