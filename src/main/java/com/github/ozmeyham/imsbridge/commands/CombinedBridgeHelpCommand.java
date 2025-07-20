package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CombinedBridgeHelpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbridgehelp";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridgehelp";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "=== Combined Bridge Commands ==="));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/cbridge chat " + EnumChatFormatting.WHITE + "- Toggle chat forwarding"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/bc <msg> " + EnumChatFormatting.WHITE + "- Send a message to combined bridge"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/cbridgecolour <c1> <c2> <c3> " + EnumChatFormatting.WHITE + "- Change colours"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/cbridgetoggle " + EnumChatFormatting.WHITE + "- Toggle receiving combined messages"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
