package com.github.ozmeyham.imsbridge.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class TextUtils {
    // Print message to chat
    public static void printToChat(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§aIMS-Bridge Mod > §r" + msg));
    }

    // Lightweight JSON string escaper
    public static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}