package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.client.Constants;
import dev.isxander.yacl3.api.OptionDescription;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class YACLUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    // https://github.com/mrbuilder1961/ChatPatches/blob/1.21.4/src/main/java/obro1961/chatpatches/config/YACLConfig.java#L337
    public static OptionDescription descWithImage(String translatable, String img) {
        OptionDescription.Builder builder = OptionDescription.createBuilder().text(
                Component.translatable(translatable)
        );

        String image = "textures/preview/" + img + ".webp";
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID, image);

        try {
            if( mc.getResourceManager().getResource(location).isPresent() )
                builder.webpImage(location);
            else
                Constants.LOG.debug("[YACLConfig.desc] Couldn't find '{}'", image);
        } catch(Throwable e) {
            Constants.LOG.error("[YACLConfig.desc] An error occurred while trying to use '{}:{}' :", Constants.MOD_ID, image, e);
        }

        return builder.build();
    }
}
