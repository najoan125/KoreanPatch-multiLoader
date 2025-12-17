package com.hyfata.najoan.koreanpatch.config;

import com.hyfata.najoan.koreanpatch.config.screen.ModernSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModernSettingsScreen::new;
    }
}
