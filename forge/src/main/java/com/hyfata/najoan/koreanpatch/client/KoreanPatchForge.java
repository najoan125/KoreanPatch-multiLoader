package com.hyfata.najoan.koreanpatch.client;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;

@Mod(value = Constants.MOD_ID)
public class KoreanPatchForge {

    public KoreanPatchForge(IEventBus bus, ModContainer container) {
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        KoreanPatchClient.init();

        KeyBindsForge.register();
        bus.addListener(this::registerKeys);
    }

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindsForge.getImeBinding());
        event.register(KeyBindsForge.getLangBinding());
	}
}
