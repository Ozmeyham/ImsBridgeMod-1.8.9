package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeMsgCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "bc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bc <message>: Sends a message to combined bridge.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            String message = String.join(" ", args);
            if (!combinedBridgeEnabled) {
                printToChat("§cYou have to enable combined bridge to use this command! §6§o/cbridge toggle");
            } else {
                if (wsClient != null && wsClient.isOpen()) { // Check if WebSocket client is connected
                    wsClient.send("{\"from\":\"mc\",\"msg\":\"" + message + "\",\"combinedbridge\":true}");
                } else {
                    printToChat("§cWebSocket client not connected. Please ensure IMSBridge is running and connected.");
                }
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {
        return null;
    }
}