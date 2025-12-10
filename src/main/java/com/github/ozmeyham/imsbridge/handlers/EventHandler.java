package com.github.ozmeyham.imsbridge.handlers;

import net.minecraftforge.common.MinecraftForge;

public class EventHandler {
    public static void eventListener() {
        MinecraftForge.EVENT_BUS.register(new ClientChatHandler());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        MinecraftForge.EVENT_BUS.register(new WorldJoinHandler());
    }
}
