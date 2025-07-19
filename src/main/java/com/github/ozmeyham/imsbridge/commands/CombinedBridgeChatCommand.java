package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

// Import your custom utility classes for Forge 1.8.9
import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeChatEnabled; // Assuming this is public static in IMSBridge
import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled; // Assuming this is public static in IMSBridge
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig; // Assuming you might want to reload config after changes
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat; // Re-using the printToChat from the previous example

public class CombinedBridgeChatCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "cbridge"; // The base command is "cbridge"
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge chat: Toggles combined bridge chat messages.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // We are looking for "/cbridge chat"
        if (args.length == 1 && args[0].equalsIgnoreCase("chat")) {
            if (!combinedBridgeChatEnabled) { // Equivalent to combinedBridgeChatEnabled == false
                if (combinedBridgeEnabled) { // Check if main cbridge is enabled
                    combinedBridgeChatEnabled = true;
                    saveConfigValue("combinedBridgeChatEnabled", "true");
                    printToChat("§aEnabled combined bridge chat! §e§oThis functions like /chat guild, but for cbridge."); // Note: §i is not supported in 1.8.9 for italic, use §o instead
                } else {
                    printToChat("§cYou need to enable cbridge messages before using this command! Do /cbridge toggle");
                }
            } else { // If combinedBridgeChatEnabled is true
                combinedBridgeChatEnabled = false;
                saveConfigValue("combinedBridgeChatEnabled", "false");
                printToChat("§cDisabled Combined Bridge Chat!");
            }
            // After changing config values, consider reloading if your mod relies on it
            loadConfig();
        } else {
            // If the command is just "/cbridge" or has other arguments,
            // you might want to show help or delegate to other cbridge subcommands.
            // For now, it just shows usage.
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Allow all players to use this client-side command
    }

    // Optional: for tab completion
    @Override
    public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "chat");
        }
        return null;
    }
}