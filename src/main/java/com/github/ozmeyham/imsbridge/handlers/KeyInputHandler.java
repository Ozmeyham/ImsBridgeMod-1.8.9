// src/main/java/com/github/ozmeyham/imsbridge/handlers/KeyInputHandler.java
package com.github.ozmeyham.imsbridge.handlers;

import com.github.ozmeyham.imsbridge.gui.PrefixingChatGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static com.github.ozmeyham.imsbridge.IMSBridge.*;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.*;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;

public class KeyInputHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        KeyBinding chatKey = mc.gameSettings.keyBindChat;
        KeyBinding commandKey = mc.gameSettings.keyBindCommand;

        // when the vanilla chat GUI opens and cbridge is enabled, replace with PrefixingChatGui to handle cbridge msgs
        if (combinedBridgeEnabled && combinedBridgeChatEnabled && wsClient != null && bridgeKey != null) {
            if (chatKey.isPressed()) {
                mc.displayGuiScreen(new PrefixingChatGui());
            } else if (commandKey.isPressed()) {
                mc.displayGuiScreen(new PrefixingChatGui("/"));
            }
        }
    }
}
