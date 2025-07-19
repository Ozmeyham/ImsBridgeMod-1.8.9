package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CombinedBridgeHelpCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge help: Shows this message.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.addChatMessage(new ChatComponentText(
                    "\n§c/cbridge toggle: §fEnables/disables client-side cbridge message rendering.\n" +
                            "§c/cbridge colour <colour1> <colour2> <colour3>: §fSets the colour formatting of rendered bridge messages.\n" +
                            "§c/cbridge colour: §fSets the colour formatting back to default.\n" +
                            "§c/cbridge chat: §fEnable/disable sending cbridge messages with no command prefix (like /chat guild)\n" +
                            "§c/bc <msg>: §fSends msg to cbridge, all other connected players can see this in game.\n" +
                            "§c/cbridge help: §fShows this message.\n"
            ));
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "help");
        }
        return null;
    }
}