package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import static com.github.ozmeyham.imsbridge.IMSBridge.*;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;

public class CombinedBridgeChatCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge chat - Toggle combined bridge chat mode";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("chat")) {
            if (!combinedBridgeEnabled) {
                sender.addChatMessage(new ChatComponentText("§cEnable cbridge messages first!"));
                return;
            }

            combinedBridgeChatEnabled = !combinedBridgeChatEnabled;
            saveConfigValue("combinedBridgeChatEnabled", String.valueOf(combinedBridgeChatEnabled));
            sender.addChatMessage(new ChatComponentText(combinedBridgeChatEnabled ?
                    "§aEnabled combined bridge chat!" : "§cDisabled combined bridge chat!"));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}