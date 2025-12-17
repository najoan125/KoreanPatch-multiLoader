package com.hyfata.najoan.koreanpatch.config.category;

import com.hyfata.najoan.koreanpatch.config.gson.JsonComment;
import java.util.ArrayList;
import java.util.List;

/**
 * 키바인딩 설정 관리 클래스
 * 다중 키 조합을 지원합니다.
 */
public class CategoryKeyBindings {
    @JsonComment(value = "한/영 변환키 목록 (최대 2개). 각 키는 다중 키 조합을 배열로 표현")
    private List<List<Integer>> langTypeKeys = new ArrayList<>();

    @JsonComment(value = "IME 토글 키 목록. 각 키는 다중 키 조합을 배열로 표현")
    private List<List<Integer>> imeKeys = new ArrayList<>();

    public CategoryKeyBindings() {
        // 기본값 초기화
        initializeDefaults();
    }

    private void initializeDefaults() {
        // 기본 한/영 변환키 설정
        langTypeKeys.clear();
        List<Integer> defaultLangKey1 = new ArrayList<>();

        // OS에 따른 기본값 설정은 KeyBindingManager에서 처리

        // 기본 IME 토글 키: I (23)
        imeKeys.clear();
        List<Integer> defaultImeKey = new ArrayList<>();
        defaultImeKey.add(23); // GLFW_KEY_I
    }

    /**
     * 한/영 변환키 목록 가져오기
     * @return 다중 키 조합 목록
     */
    public List<List<Integer>> getLangTypeKeys() {
        return langTypeKeys;
    }

    /**
     * 한/영 변환키 목록 설정
     * @param keys 다중 키 조합 목록
     */
    public void setLangTypeKeys(List<List<Integer>> keys) {
        this.langTypeKeys = keys;
    }

    /**
     * 한/영 변환키 추가 (최대 2개)
     * @param keyCombo 키 조합 (예: [29, 23] = Ctrl + I)
     */
    public void addLangTypeKey(List<Integer> keyCombo) {
        if (langTypeKeys.size() < 2) {
            langTypeKeys.add(new ArrayList<>(keyCombo));
        }
    }

    /**
     * 한/영 변환키 제거
     * @param index 제거할 키의 인덱스
     */
    public void removeLangTypeKey(int index) {
        if (index >= 0 && index < langTypeKeys.size()) {
            langTypeKeys.remove(index);
        }
    }

    /**
     * IME 토글 키 목록 가져오기
     * @return 다중 키 조합 목록
     */
    public List<List<Integer>> getImeKeys() {
        return imeKeys;
    }

    /**
     * IME 토글 키 목록 설정
     * @param keys 다중 키 조합 목록
     */
    public void setImeKeys(List<List<Integer>> keys) {
        this.imeKeys = keys;
    }

    /**
     * IME 토글 키 추가
     * @param keyCombo 키 조합
     */
    public void addImeKey(List<Integer> keyCombo) {
        imeKeys.add(new ArrayList<>(keyCombo));
    }

    /**
     * IME 토글 키 제거
     * @param index 제거할 키의 인덱스
     */
    public void removeImeKey(int index) {
        if (index >= 0 && index < imeKeys.size()) {
            imeKeys.remove(index);
        }
    }

    /**
     * 모든 키 조합 초기화
     */
    public void resetToDefaults() {
        initializeDefaults();
    }
}
