package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.ImsWebSocketClient;

public class CombinedBridgeMsgCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bc <message>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new CommandException("Usage: " + getCommandUsage(sender));
        }

        // assemble your full message
        String msg = joinStrings(args, 0);

        if (IMSBridge.combinedBridgeEnabled
                && ImsWebSocketClient.wsClient != null
                && ImsWebSocketClient.wsClient.isOpen()) {
            // send over the bridge
            ImsWebSocketClient.wsClient.send("{\"from\":\"mc\",\"msg\":\"" + msg + "\",\"combinedbridge\":true}");
            sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GREEN + "Sent to combined bridge: " + msg
            ));
        } else {
            sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + "Combined bridge is not enabled or not connected."
            ));
        }
    }

    /**
     * Simple helper to join an array of strings with spaces,
     * starting at the given index.
     */
    private static String joinStrings(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            if (i > startIndex) {
                sb.append(' ');
            }
            sb.append(args[i]);
        }
        return sb.toString();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
