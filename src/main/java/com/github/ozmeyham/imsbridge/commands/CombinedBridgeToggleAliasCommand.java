package com.github.ozmeyham.imsbridge.commands;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CombinedBridgeToggleAliasCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbt";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        // === copied from CombinedBridgeCommands#case "toggle" ===
        IMSBridge.combinedBridgeEnabled = !IMSBridge.combinedBridgeEnabled;
        ConfigUtils.saveConfigValue(
                "combinedBridgeEnabled",
                String.valueOf(IMSBridge.combinedBridgeEnabled)
        );
        TextUtils.printToChat(
                IMSBridge.combinedBridgeEnabled
                        ? "§aEnabled combined bridge messages!"
                        : "§cDisabled combined bridge messages!"
        );
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
