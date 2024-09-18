package com.hyfata.najoan.koreanpatch.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class KoreanPatchFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
//        KoreanPatchClient.init();

        registerEvents(); // temp
        KeyBindsFabric.register();
    }

    // temp
    public void registerEvents() {
        ClientLifecycleEvents.CLIENT_STARTED.register(EventListenerFabric::onClientStarted);
        ScreenEvents.AFTER_INIT.register(EventListenerFabric::afterScreenChange);
        ClientTickEvents.END_CLIENT_TICK.register(EventListenerFabric::onClientTick);
    }
}
