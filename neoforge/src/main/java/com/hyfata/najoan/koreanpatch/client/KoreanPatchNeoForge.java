package com.hyfata.najoan.koreanpatch.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KoreanPatchNeoForge {

    public KoreanPatchNeoForge(IEventBus bus, ModContainer container) {
        KoreanPatchClient.init();
        bus.addListener(this::registerKeys);

        registerEvents(bus);
    }

    public void registerEvents(IEventBus bus) {
        bus.addListener(EventListenerNeoForge::onClientStarted);
        NeoForge.EVENT_BUS.addListener(EventListenerNeoForge::onClientTick);
        NeoForge.EVENT_BUS.addListener(EventListenerNeoForge::afterScreenChange);
    }

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBinds.getImeBinding());
        event.register(KeyBinds.getLangBinding());
	}
}
