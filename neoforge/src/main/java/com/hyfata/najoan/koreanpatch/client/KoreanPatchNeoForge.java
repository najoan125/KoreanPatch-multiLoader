package com.hyfata.najoan.koreanpatch.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KoreanPatchNeoForge {

    public KoreanPatchNeoForge(IEventBus bus, ModContainer container) {
        KoreanPatchClient.init();

        KeyBindsNeoForge.register();
        bus.addListener(this::registerKeys);
    }

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindsNeoForge.getImeBinding());
        event.register(KeyBindsNeoForge.getLangBinding());
	}
}
