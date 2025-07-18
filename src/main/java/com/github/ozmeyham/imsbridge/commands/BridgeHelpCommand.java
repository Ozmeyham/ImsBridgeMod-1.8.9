package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class BridgeHelpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge help - Show bridge commands help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            sender.addChatMessage(new ChatComponentText(
                    "\n§c/bridgekey <key>: §fSets your bridge key\n" +
                            "§c/bridge toggle: §fToggles bridge messages\n" +
                            "§c/bridge colour: §fSets message colors\n" +
                            "§c/bridge help: §fShows this help\n"
            ));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}