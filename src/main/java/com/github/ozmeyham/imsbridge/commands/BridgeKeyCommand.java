package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.connectWebSocket; // Ensure this method exists and is compatible
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.isValidBridgeKey; // Ensure this method exists and is compatible
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class BridgeKeyCommand extends CommandBase {

    private static final Logger LOGGER = LogManager.getLogger("IMSBridge"); // Use your mod ID or a suitable name

    @Override
    public String getCommandName() {
        return "bridgekey";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridgekey <key>: Sets your bridge key to use the mod.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            String key = args[0];
            bridgeKey = key;

            if (isValidBridgeKey()) {
                saveConfigValue("bridgeKey", bridgeKey);
                loadConfig();

                LOGGER.info("Bridge key set to " + key);
                printToChat("§cBridge key saved as: §f" + bridgeKey);

                connectWebSocket();
            } else {
                printToChat("§cInvalid bridge key format! Check you pasted correctly.");
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }


    @Override
    public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {

        return null;
    }
}