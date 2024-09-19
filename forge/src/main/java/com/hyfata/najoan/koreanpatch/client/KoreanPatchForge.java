package com.hyfata.najoan.koreanpatch.client;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(value = Constants.MOD_ID)
public class KoreanPatchForge {

    public KoreanPatchForge() {
        IEventBus bus = Mod.EventBusSubscriber.Bus.MOD.bus().get();
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        init(bus, container);
    }

    private KoreanPatchForge(IEventBus bus, ModContainer container) {
        KoreanPatchClient.init();

        KeyBindsForge.register();
        bus.addListener(this::registerKeys);
    }

    private void init(IEventBus bus, ModContainer container) {
        new KoreanPatchForge(bus, container);
    }

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindsForge.getImeBinding());
        event.register(KeyBindsForge.getLangBinding());
	}
}
