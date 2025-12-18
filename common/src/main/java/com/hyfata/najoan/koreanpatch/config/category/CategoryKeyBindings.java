package com.hyfata.najoan.koreanpatch.config.category;

import com.hyfata.najoan.koreanpatch.config.gson.JsonComment;
import com.hyfata.najoan.koreanpatch.keybinding.KeyIdentifier;

import java.util.ArrayList;
import java.util.List;

public class CategoryKeyBindings {
    @JsonComment(value = "List of language toggle keys (maximum 2). Each key represents a multi-key combination as an array")
    private final List<List<KeyIdentifier>> langTypeKeys = new ArrayList<>();

    @JsonComment(value = "List of IME toggle keys. Each key represents a multi-key combination as an array")
    private final List<List<KeyIdentifier>> imeKeys = new ArrayList<>();

    public List<List<KeyIdentifier>> getLangTypeKeys() {
        return langTypeKeys;
    }

    public void addLangTypeKey(List<KeyIdentifier> keyCombo) {
        if (langTypeKeys.size() < 2) {
            langTypeKeys.add(new ArrayList<>(keyCombo));
        }
    }

    public void removeLangTypeKey(int index) {
        if (index >= 0 && index < langTypeKeys.size()) {
            langTypeKeys.remove(index);
        }
    }

    public List<List<KeyIdentifier>> getImeKeys() {
        return imeKeys;
    }

    public void addImeKey(List<KeyIdentifier> keyCombo) {
        imeKeys.add(new ArrayList<>(keyCombo));
    }

    public void removeImeKey(int index) {
        if (index >= 0 && index < imeKeys.size()) {
            imeKeys.remove(index);
        }
    }
}
