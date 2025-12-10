package com.github.ozmeyham.imsbridge.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import static com.github.ozmeyham.imsbridge.IMSBridge.checkedForUpdate;
import static com.github.ozmeyham.imsbridge.IMSBridge.firstLogin;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.*;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.isValidBridgeKey;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class WorldJoinHandler {
    @SubscribeEvent
    public void onWorldJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        clientOnline = true;
        if (isValidBridgeKey()) {
            connectWebSocket();
            System.out.println("Bridge key valid! Connecting to websocket...");
        }
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                if (firstLogin) {
                    firstLogin = false;
                    saveConfigValue("firstLogin", "false");
                    printToChat("§bThanks for installing IMS Bridge Mod.\n§7Use §6/bridge help §7and §6/cbridge help §7to get a list of all commands.");
                }
                Thread.sleep(2000);
                if (!checkedForUpdate) {
                    // checkForUpdates(); //wip for now
                    checkedForUpdate = true;
                }
                if (!isValidBridgeKey()) {
                    printToChat("§cBridge key not set. §7Use §6/key §7on discord to obtain a key, then run §6/bridge key §7in-game and paste your key.");
                }

            } catch (InterruptedException e) {
                printToChat("§cAn error occured: "+e);
                throw new RuntimeException(e);
            }
        }).start();
    }

    @SubscribeEvent
    public void onWorldLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        clientOnline = false;
        disconnectWebSocket();
    }
}
