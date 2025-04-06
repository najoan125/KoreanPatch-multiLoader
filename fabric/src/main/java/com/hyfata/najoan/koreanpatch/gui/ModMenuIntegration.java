package com.hyfata.najoan.koreanpatch.gui;

import com.hyfata.najoan.koreanpatch.gui.yacl.YaclConfigScreenFactoryManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return YaclConfigScreenFactoryManager::createScreen;
    }
}
