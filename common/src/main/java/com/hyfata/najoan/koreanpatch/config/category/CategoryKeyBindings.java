package com.hyfata.najoan.koreanpatch.config.category;

import com.hyfata.najoan.koreanpatch.config.gson.JsonComment;
import com.hyfata.najoan.koreanpatch.keybinding.KeyIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * 키바인딩 설정 관리 클래스
 * 다중 키 조합을 지원합니다.
 * KeyIdentifier를 사용하여 keyCode 또는 scanCode로 키를 식별합니다.
 */
public class CategoryKeyBindings {
    @JsonComment(value = "한/영 변환키 목록 (최대 2개). 각 키는 다중 키 조합을 배열로 표현")
    private final List<List<KeyIdentifier>> langTypeKeys = new ArrayList<>();

    @JsonComment(value = "IME 토글 키 목록. 각 키는 다중 키 조합을 배열로 표현")
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
