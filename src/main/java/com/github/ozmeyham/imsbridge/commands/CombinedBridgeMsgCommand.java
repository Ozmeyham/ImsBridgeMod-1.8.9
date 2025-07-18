package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.wsClient;

public class CombinedBridgeMsgCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bc <message> - Send message to combined bridge";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!combinedBridgeEnabled) {
            sender.addChatMessage(new ChatComponentText("§cEnable cbridge first!"));
            return;
        }

        if (args.length > 0) {
            String message = String.join(" ", args);
            wsClient.send("{\"from\":\"mc\",\"msg\":\"" + message + "\",\"combinedbridge\":true}");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}