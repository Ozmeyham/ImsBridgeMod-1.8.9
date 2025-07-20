package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class BridgeHelpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bridgehelp";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridgehelp";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "=== IMS Bridge Commands ==="));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/bridgekey <key> " + EnumChatFormatting.WHITE + "- Set your bridge key"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/bridge toggle " + EnumChatFormatting.WHITE + "- Enable/disable guild messages"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/bridgecolour <c1> <c2> <c3> " + EnumChatFormatting.WHITE + "- Change guild message colours"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
