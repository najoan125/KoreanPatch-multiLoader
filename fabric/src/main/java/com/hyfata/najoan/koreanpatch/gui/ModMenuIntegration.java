package com.hyfata.najoan.koreanpatch.gui;

import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new Screen(new TextComponent("")) {
            @Override
            protected void init() {
                ConfigManager.getInstance().openConfigFile();
                if (this.minecraft != null) {
                    this.minecraft.setScreen(screen);
                }
            }
        };
    }
}
