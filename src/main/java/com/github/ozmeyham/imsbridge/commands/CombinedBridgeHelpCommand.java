package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CombinedBridgeHelpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge help - Show cbridge commands help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            sender.addChatMessage(new ChatComponentText(
                    "\n§c/cbridge toggle: §fToggle cbridge messages\n" +
                            "§c/cbridge colour: §fSet message colors\n" +
                            "§c/cbridge chat: §fToggle chat mode\n" +
                            "§c/bc <msg>: §fSend message to cbridge\n" +
                            "§c/cbridge help: §fShow this help\n"
            ));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}