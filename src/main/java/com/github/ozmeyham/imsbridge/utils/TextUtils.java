package com.github.ozmeyham.imsbridge.utils;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextUtils {
    // Simple in-game chat print because the command is so long for some reason
    public static void printToChat(String msg) {
        FMLClientHandler.instance().getClient().addScheduledTask(() -> {
            IChatComponent component = new ChatComponentText("§6IMS-Bridge Mod > §r" + msg);
            FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(component);
        });
    }
    // client message sanitizer
    public static String sanitizeMessage (String msg) {
        return msg.replace("\"","''").replace("\\","\\\\");
    }

    // JSON string escaper
    public static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    public static Boolean isSkyblockChannelChange(String content) {
        Set<String> validMessages = new HashSet<String>(Arrays.asList(
                "You're already in this channel!",
                "You are now in the GUILD channel",
                "You are now in the ALL channel",
                "You are now in the PARTY channel",
                "You are now in the OFFICER channel",
                "You are now in the SKYBLOCK CO-OP channel"
        ));
        return validMessages.contains(content);
    }

    public static Boolean isGuildChatToggled(String content) {
        Set<String> toggleMessage = new HashSet<String>(Arrays.asList(
                "Disabled guild chat!",
                "Enabled guild chat!"
        ));
        return toggleMessage.contains(content);
    }

}
