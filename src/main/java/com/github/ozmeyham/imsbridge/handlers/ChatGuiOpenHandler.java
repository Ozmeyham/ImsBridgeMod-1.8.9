// src/main/java/com/github/ozmeyham/imsbridge/handlers/ChatGuiOpenHandler.java
package com.github.ozmeyham.imsbridge.handlers;

import com.github.ozmeyham.imsbridge.gui.PrefixingChatGui;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiChat;

public class ChatGuiOpenHandler {
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChat
                && !(event.gui instanceof PrefixingChatGui)) {
            // simply swap in your custom chat GUI;
            // we’re dropping the “carry‐over” text because inputField is protected.
            event.gui = new PrefixingChatGui();
        }
    }
}
