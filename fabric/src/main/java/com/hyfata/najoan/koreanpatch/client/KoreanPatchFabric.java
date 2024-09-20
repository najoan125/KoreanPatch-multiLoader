package com.hyfata.najoan.koreanpatch.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class KoreanPatchFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KoreanPatchClient.init();
        registerEvents();
    }

    public void registerEvents() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> EventListener.onClientStarted());
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> EventListener.afterScreenChange(screen));
        ClientTickEvents.END_CLIENT_TICK.register(client -> EventListener.onClientTick());
    }
}
