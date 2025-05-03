package com.hyfata.najoan.koreanpatch.data.config.category;

import com.hyfata.najoan.koreanpatch.data.config.category.input.AutoLangTypeMode;
import com.hyfata.najoan.koreanpatch.data.gson.JsonComment;

public class CategoryInput {
    @JsonComment(value = "Auto language type mode: ", enums = true)
    private AutoLangTypeMode autoLangTypeMode = AutoLangTypeMode.AUTO;
    private boolean memoryLangTypePerScreen = false;
    private boolean autoImeSwitch = true;

    public AutoLangTypeMode getAutoLangTypeMode() {
        return autoLangTypeMode;
    }

    public void setAutoLangTypeMode(AutoLangTypeMode autoLangTypeMode) {
        this.autoLangTypeMode = autoLangTypeMode;
    }

    public boolean isMemoryLangTypePerScreen() {
        return memoryLangTypePerScreen;
    }

    public void setMemoryLangTypePerScreen(boolean memoryLangTypePerScreen) {
        this.memoryLangTypePerScreen = memoryLangTypePerScreen;
    }

    public boolean isAutoImeSwitch() {
        return autoImeSwitch;
    }

    public void setAutoImeSwitch(boolean autoImeSwitch) {
        this.autoImeSwitch = autoImeSwitch;
    }
}
