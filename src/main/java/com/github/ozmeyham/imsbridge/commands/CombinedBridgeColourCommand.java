package com.github.ozmeyham.imsbridge.commands;



import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;

public class CombinedBridgeColourCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbridgecolour";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridgecolour <colour1> <colour2> <colour3>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 3) {
            throw new CommandException("Usage: " + getCommandUsage(sender));
        }

        String c1 = args[0];
        String c2 = args[1];
        String c3 = args[2];

        IMSBridge.cbridgeC1 = c1;
        IMSBridge.cbridgeC2 = c2;
        IMSBridge.cbridgeC3 = c3;

        ConfigUtils.saveConfigValue("cbridge_colour1", c1);
        ConfigUtils.saveConfigValue("cbridge_colour2", c2);
        ConfigUtils.saveConfigValue("cbridge_colour3", c3);

        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + "Combined bridge colours updated: "
                        + c1 + "c1 " + c2 + "c2 " + c3 + "c3"
        ));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
