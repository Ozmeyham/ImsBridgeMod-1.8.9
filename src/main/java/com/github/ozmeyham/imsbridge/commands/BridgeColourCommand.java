package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.ozmeyham.imsbridge.IMSBridge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class BridgeColourCommand extends CommandBase {
    private static final Set<String> VALID_NAMES = new HashSet<>(Arrays.asList(
            "black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple",
            "gold", "gray", "dark_gray", "blue", "green", "aqua", "red",
            "light_purple", "yellow", "white"
    ));

    @Override
    public String getCommandName() {
        return "bridgecolour";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridgecolour <colour1> <colour2> <colour3>\n" +
                "Valid names: " + VALID_NAMES;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 3) {
            throw new CommandException("Usage: " + getCommandUsage(sender));
        }

        // Validate names
        String[] lower = new String[3];
        for (int i = 0; i < 3; i++) {
            String name = args[i].toLowerCase(Locale.ROOT);
            if (!VALID_NAMES.contains(name)) {
                throw new CommandException("Invalid colour: " + args[i] +
                        ". Valid names: " + VALID_NAMES);
            }
            lower[i] = name;
        }

        // Map names → §-code using EnumChatFormatting
        EnumChatFormatting fmt1 = EnumChatFormatting.valueOf(lower[0].toUpperCase(Locale.ROOT));
        EnumChatFormatting fmt2 = EnumChatFormatting.valueOf(lower[1].toUpperCase(Locale.ROOT));
        EnumChatFormatting fmt3 = EnumChatFormatting.valueOf(lower[2].toUpperCase(Locale.ROOT));

        IMSBridge.bridgeC1 = fmt1.toString();
        IMSBridge.bridgeC2 = fmt2.toString();
        IMSBridge.bridgeC3 = fmt3.toString();

        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + "Bridge colours updated to: " +
                        lower[0] + ", " + lower[1] + ", " + lower[2]
        ));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
