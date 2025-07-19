package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

// Import your custom utility classes for Forge 1.8.9
import java.util.List;

import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig; // Assuming you'd want to reload config after setting
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat; // Re-using the printToChat from the previous example

// You'll need to manage this boolean state somewhere.
// It could be in your main mod class or in a dedicated state manager.
// For simplicity, I'll assume it's publicly accessible, e.g., in IMSBridge class or a new BridgeSettings class.
// For this example, I'll put it directly in this command class for demonstration,
// but for a larger mod, consider a more structured approach.
import static com.github.ozmeyham.imsbridge.IMSBridge.bridgeEnabled; // Assuming bridgeEnabled is now managed globally, e.g., in IMSBridge

public class BridgeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge toggle: Toggles bridge messages on/off.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            if (bridgeEnabled) {
                bridgeEnabled = false;
                saveConfigValue("bridgeEnabled", "false"); // Save boolean as string in config
                printToChat("§cDisabled bridge messages!");
            } else {
                bridgeEnabled = true;
                saveConfigValue("bridgeEnabled", "true"); // Save boolean as string in config
                printToChat("§2Enabled bridge messages!");
            }
            // After changing a config value, it's often good practice to reload your config
            // if other parts of your mod read from the config file directly rather than memory.
            loadConfig();
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Allows any player to use this client-side command
    }

    // Optional: for tab completion
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "toggle");
        }
        return null;
    }
}