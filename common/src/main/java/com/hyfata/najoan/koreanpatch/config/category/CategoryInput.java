package com.hyfata.najoan.koreanpatch.config.category;

import com.hyfata.najoan.koreanpatch.config.category.input.AutoLangTypeMode;
import com.hyfata.najoan.koreanpatch.config.gson.JsonComment;

public class CategoryInput {
    @JsonComment(value = "Auto language type mode: ", enums = true)
    private AutoLangTypeMode autoLangTypeMode = AutoLangTypeMode.AUTO;
    private boolean memoryLangTypePerScreen = false;
    private boolean disableImeWhenPlaying = true;
    private boolean autoImeSwitch = true;
    private boolean alwaysImeEnabled = false;

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

    public boolean isDisableImeWhenPlaying() {
        return disableImeWhenPlaying;
    }

    public void setDisableImeWhenPlaying(boolean disableImeWhenPlaying) {
        this.disableImeWhenPlaying = disableImeWhenPlaying;
    }

    public boolean isAlwaysImeEnabled() {
        return alwaysImeEnabled;
    }

    public void setAlwaysImeEnabled(boolean alwaysImeEnabled) {
        this.alwaysImeEnabled = alwaysImeEnabled;
    }
}
