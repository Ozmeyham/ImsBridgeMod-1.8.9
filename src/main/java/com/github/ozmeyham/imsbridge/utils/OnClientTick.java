package com.github.ozmeyham.imsbridge.utils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.github.ozmeyham.imsbridge.IMSBridge.delayTicks;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.isValidBridgeKey;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.shouldCheckKey;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class OnClientTick {
    public OnClientTick() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.phase != TickEvent.Phase.END) return;
        if (!shouldCheckKey ||mc.thePlayer == null) return;

        if (delayTicks > 0) {
            delayTicks--;
        } else {
            if (!isValidBridgeKey()) {
                printToChat("§cBridge key not set. §6Use /key on Discord to obtain a key, then run /bridgekey in-game and paste your key.");
            }
            shouldCheckKey = false;
            }
    }
}
