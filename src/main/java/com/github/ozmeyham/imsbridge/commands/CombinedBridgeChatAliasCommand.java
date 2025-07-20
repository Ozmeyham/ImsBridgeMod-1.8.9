// src/main/java/com/github/ozmeyham/imsbridge/commands/CombinedBridgeChatAliasCommand.java
package com.github.ozmeyham.imsbridge.commands;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CombinedBridgeChatAliasCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbc";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        // exact same toggle logic as in CombinedBridgeCommand's "chat" case
        IMSBridge.combinedBridgeChatEnabled = !IMSBridge.combinedBridgeChatEnabled;
        ConfigUtils.saveConfigValue(
                "combinedBridgeChatEnabled",
                String.valueOf(IMSBridge.combinedBridgeChatEnabled)
        );
        TextUtils.printToChat(
                IMSBridge.combinedBridgeChatEnabled
                        ? "§aEnabled combined bridge chat!"
                        : "§cDisabled combined bridge chat!"
        );
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
