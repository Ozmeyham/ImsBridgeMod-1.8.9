package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.github.ozmeyham.imsbridge.IMSBridge;

import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class BridgeOnlineAliasCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bl";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bl";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (wsClient != null && wsClient.isOpen()) {
            wsClient.send("{\"from\":\"mc\",\"request\":\"getOnlinePlayers\"}");
        } else {
            printToChat("§cYou are not connected to the bridge server!");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
