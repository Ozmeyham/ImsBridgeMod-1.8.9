// src/main/java/com/github/ozmeyham/imsbridge/gui/PrefixingChatGui.java
package com.github.ozmeyham.imsbridge.gui;

import com.github.ozmeyham.imsbridge.IMSBridge;
import net.minecraft.client.gui.GuiChat;

public class PrefixingChatGui extends GuiChat {
    public PrefixingChatGui() {
        super();
    }

    public PrefixingChatGui(String defaultText) {
        super(defaultText);
    }

    @Override
    public void sendChatMessage(String text) {
        // if combined‐chat is enabled AND it’s NOT a slash command…
        if (IMSBridge.combinedBridgeChatEnabled && !text.startsWith("/")) {
            // prefix with /bc and send
            super.sendChatMessage("/cbc " + text);
        } else {
            // otherwise behave exactly like vanilla
            super.sendChatMessage(text);
        }
    }
}
