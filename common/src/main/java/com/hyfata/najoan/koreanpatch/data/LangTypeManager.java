package com.hyfata.najoan.koreanpatch.data;

import com.hyfata.najoan.koreanpatch.data.provider.LanguageType;
import com.hyfata.najoan.koreanpatch.process.ime.InputManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;

public class LangTypeManager {
    private static LangTypeManager instance;

    public static LangTypeManager getInstance() {
        if (instance == null) {
            instance = new LangTypeManager();
        }
        return instance;
    }

    private final Minecraft client = Minecraft.getInstance();
    private LanguageType currentType = LanguageType.EN;
    private final Component IME_TEXT = new TextComponent("IME");

    public void setCurrentType(LanguageType type) {
        currentType = type;
    }

    public LanguageType getCurrentType() {
        return currentType;
    }

    public boolean isKorean() {
        if (!ConfigManager.getInstance().getConfig().getCategoryInput().isAlwaysImeEnabled())
            return currentType == LanguageType.KO && !InputManager.getController().isFocused();
        return currentType == LanguageType.KO;
    }

    public void toggleCurrentType() {
        currentType = currentType == LanguageType.KO ? LanguageType.EN : LanguageType.KO;
    }

    public FormattedCharSequence getCurrentText() {
        if (InputManager.getController().isFocused() &&
                !ConfigManager.getInstance().getConfig().getCategoryInput().isAlwaysImeEnabled()) {
            return IME_TEXT.getVisualOrderText();
        }
        return currentType.getTranslatedVisualOrderText();
    }

    public int getCurrentTextWidth() {
        return client.font.width(getCurrentText());
    }
}