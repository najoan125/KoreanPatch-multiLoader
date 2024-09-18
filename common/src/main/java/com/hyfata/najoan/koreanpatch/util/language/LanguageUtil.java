package com.hyfata.najoan.koreanpatch.util.language;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class LanguageUtil {
    public static final int EN = 0;
    public static final int KO = 1;
    private static int currentType = EN;

    static Minecraft client = Minecraft.getInstance();
    static Component KO_TEXT = Component.translatable("koreanpatch.langtype.korean");
    static Component EN_TEXT = Component.translatable("koreanpatch.langtype.english");

    public static int getCurrentType() {
        return currentType;
    }

    public static boolean isKorean() {
        return getCurrentType() == KO && !KoreanPatchClient.IME;
    }

    public static void setCurrentType(int currentType) {
        LanguageUtil.currentType = currentType;
    }

    public static void toggleCurrentType() {
        if (isKorean()) {
            setCurrentType(EN);
        } else {
            setCurrentType(KO);
        }
    }
    
    public static FormattedCharSequence getCurrentText() {
        if (KoreanPatchClient.IME) {
            return Component.literal("IME").getVisualOrderText();
        }
        return switch (currentType) {
            case EN -> EN_TEXT.getVisualOrderText();
            case KO -> KO_TEXT.getVisualOrderText();
            default -> throw new IllegalStateException("Unexpected value: " + currentType);
        };
    }

    public static int getCurrentTextWidth() {
        return client.font.width(getCurrentText());
    }
}
