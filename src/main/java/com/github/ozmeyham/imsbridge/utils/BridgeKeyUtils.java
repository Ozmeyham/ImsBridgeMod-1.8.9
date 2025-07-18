package com.github.ozmeyham.imsbridge.utils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.connectWebSocket;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class BridgeKeyUtils {
    public static String bridgeKey = null;
    private static int delayTicks = 0;
    public static Boolean shouldCheckKey = true;

    public static boolean uuidValidator(String uuid) {
        String regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uuid);
        return matcher.matches();
    }

    public static Boolean isValidBridgeKey() {
        return bridgeKey != null && !bridgeKey.isEmpty() && uuidValidator(bridgeKey);
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        shouldCheckKey = true;
        delayTicks = 40; // Wait ~2 seconds (20 ticks/second)
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && shouldCheckKey && Minecraft.getMinecraft().thePlayer != null) {
            if (delayTicks > 0) {
                delayTicks--;
            } else if (!isValidBridgeKey()) {
                printToChat("§cBridge key not set. §6Use /key on discord to obtain a key, then run /bridgekey in-game and paste your key.");
                shouldCheckKey = false;
            }
        }
    }

    public static void checkBridgeKey() {
        if (isValidBridgeKey()) {
            connectWebSocket("wss.ims-bridge.com");
        }
    }
}