package com.hyfata.najoan.koreanpatch.keybinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 키 조합을 나타내는 클래스
 * 여러 키가 동시에 눌려있는 상태를 표현합니다.
 */
public class KeyCombination {
    private final List<KeyIdentifier> keys;
    private final Set<KeyIdentifier> keySet;

    public KeyCombination(List<KeyIdentifier> keys) {
        this.keys = new ArrayList<>(keys);
        this.keySet = new HashSet<>(this.keys);
    }

    public KeyCombination(KeyIdentifier... keys) {
        this(new ArrayList<>(List.of(keys)));
    }

    /**
     * 이 조합에 속한 모든 키를 가져옵니다.
     */
    public List<KeyIdentifier> getKeys() {
        return new ArrayList<>(keys);
    }

    /**
     * 이 조합의 키 개수
     */
    public int size() {
        return keys.size();
    }

    /**
     * 특정 키가 포함되어 있는지 확인
     */
    public boolean contains(KeyIdentifier key) {
        return keySet.contains(key);
    }

    /**
     * 현재 눌려있는 모든 키가 이 조합에 정확히 일치하는지 확인
     */
    public boolean matches(Set<KeyIdentifier> pressedKeys) {
        if (this.keySet.size() != pressedKeys.size()) {
            return false;
        }
        for (KeyIdentifier required : this.keySet) {
            boolean found = false;
            for (KeyIdentifier pressed : pressedKeys) {
                if (required.matches(pressed)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    /**
     * 모든 키가 눌려있는지 확인 (추가 키 눌림 무시)
     */
    public boolean isPartiallyPressed(Set<KeyIdentifier> pressedKeys) {
        for (KeyIdentifier required : this.keySet) {
            boolean found = false;
            for (KeyIdentifier pressed : pressedKeys) {
                if (required.matches(pressed)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    /**
     * 사람이 읽을 수 있는 키 조합 문자열 반환
     */
    public String getDisplayName() {
        List<String> keyNames = new ArrayList<>();
        for (KeyIdentifier key : keys) {
            keyNames.add(key.getDisplayName());
        }
        return String.join(" + ", keyNames);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyCombination that)) return false;
        return keySet.equals(that.keySet);
    }

    @Override
    public int hashCode() {
        return keySet.hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
