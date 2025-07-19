package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class BridgeHelpCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        sender.addChatMessage(new ChatComponentText(
                "\\n§c/bridgekey <key>: §fSets your bridge key so that you can use the mod.\\n"+
                "§c/bridge toggle: §fEnables/disables client-side bridge message rendering.\n" +
                "§c/bridge colour <colour1> <colour2> <colour3>: §fSets the colour formatting of rendered bridge messages.\\n" +
                "§c/bridge colour: §fSets the colour formatting back to default.\\n" +
                "§c/bridge help: §fShows this message.\\n"));
    }
}