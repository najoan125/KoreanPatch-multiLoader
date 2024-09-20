package com.hyfata.najoan.koreanpatch.util.minecraft;

import com.hyfata.najoan.koreanpatch.mixin.accessor.KeyMappingAccessor;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.KeyMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeyMappingUtil {
    private static final List<KeyMapping> MODDED_KEY_BINDINGS = new ReferenceArrayList<>();

    public static KeyMapping registerKeyMapping(KeyMapping binding) {
        // This will do nothing if the category already exists.
        addCategory(binding.getCategory());
        MODDED_KEY_BINDINGS.add(binding);
        return binding;
    }

    public static List<KeyMapping> getModdedKeyBindings() {
        return MODDED_KEY_BINDINGS;
    }

    private static Map<String, Integer> getCategoryMap() {
        return KeyMappingAccessor.getCategoryMap();
    }

    public static void addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = getCategoryMap();

        if (map.containsKey(categoryTranslationKey)) {
            return;
        }

        Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
        int largestInt = largest.orElse(0);
        map.put(categoryTranslationKey, largestInt + 1);
        System.out.println(map.toString());
    }
}
