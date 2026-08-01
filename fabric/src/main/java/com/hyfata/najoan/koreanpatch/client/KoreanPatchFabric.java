package com.hyfata.najoan.koreanpatch.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.KeyMapping;

public class KoreanPatchFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KoreanPatchClient.init();
        registerKeys();
        registerEvents();
    }

    public void registerEvents() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> EventListener.onClientStarted());
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> EventListener.afterScreenChange());
        ClientTickEvents.END_CLIENT_TICK.register(client -> EventListener.onClientTick());
    }

    public void registerKeys() {
        for (KeyMapping key : KeyBinds.getKeyMappings()) {
            KeyMappingHelper.registerKeyMapping(key);
        }
    }
}
