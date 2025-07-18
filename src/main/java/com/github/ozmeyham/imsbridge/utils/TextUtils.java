package com.github.ozmeyham.imsbridge.utils;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.client.FMLClientHandler;

public class TextUtils {
    // Simple in-game chat print because the command is so long for some reason
    public static void printToChat(String msg) {
        FMLClientHandler.instance().getClient().addScheduledTask(() -> {
            IChatComponent component = new ChatComponentText("§aIMS-Bridge Mod > §r" + msg);
            FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(component);
        });
    }

    // Lightweight JSON string escaper
    public static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
