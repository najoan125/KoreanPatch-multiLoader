package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.mixin.accessor.KeyMappingAccessor;
import com.hyfata.najoan.koreanpatch.mixin.accessor.OptionsAccessor;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeyMappingUtil {
    private static final List<KeyMapping> MODDED_KEY_BINDINGS = new ReferenceArrayList<>();

    public static KeyMapping registerKeyMapping(KeyMapping binding) {
        for (KeyMapping existingKeyBindings : MODDED_KEY_BINDINGS) {
            if (existingKeyBindings == binding) {
                throw new IllegalArgumentException("Attempted to register a key binding twice: " + binding.getTranslatedKeyMessage());
            } else if (existingKeyBindings.getTranslatedKeyMessage().equals(binding.getTranslatedKeyMessage())) {
                throw new IllegalArgumentException("Attempted to register two key bindings with equal ID: " + binding.getTranslatedKeyMessage() + "!");
            }
        }

        // This will do nothing if the category already exists.
        addCategory(binding.getCategory());

        OptionsAccessor accessor = (OptionsAccessor) Minecraft.getInstance().options;
        accessor.setKeyMappings(ArrayUtils.add(accessor.getKeyMappings(), binding));
        MODDED_KEY_BINDINGS.add(binding);
        return binding;
    }

    private static Map<String, Integer> getCategoryMap() {
        return KeyMappingAccessor.getCategoryMap();
    }

    public static boolean addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = getCategoryMap();

        if (map.containsKey(categoryTranslationKey)) {
            return false;
        }

        Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
        int largestInt = largest.orElse(0);
        map.put(categoryTranslationKey, largestInt + 1);
        System.out.println(map.toString());
        return true;
    }
}
