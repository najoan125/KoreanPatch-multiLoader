package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.mixin.accessor.CreateWorldScreenGameTabAccessor;
import com.hyfata.najoan.koreanpatch.mixin.accessor.TabNavigationBarAccessor;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.handler.Indicator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {CreateWorldScreen.class})
public class CreateWorldScreenMixin extends Screen {
    protected CreateWorldScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    private TabNavigationBar tabNavigationBar;

    @Unique
    AnimationUtil koreanPatch$animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TabNavigationBarAccessor tabInvoker = (TabNavigationBarAccessor) tabNavigationBar;
        Tab currentTab = tabInvoker.getTabManager().getCurrentTab();

        if (currentTab instanceof CreateWorldScreen.GameTab) {
            KoreanPatchClient.bypassInjection = false;
            CreateWorldScreenGameTabAccessor gameTabAccessor = (CreateWorldScreenGameTabAccessor) currentTab;
            EditBox worldNameField = gameTabAccessor.getNameEdit();
            Component text = Component.translatable("selectWorld.enterName");

            float x = EditBoxUtil.getCursorXWithText(worldNameField, text, worldNameField.getX()) + 4;
            float y = EditBoxUtil.calculateIndicatorY(worldNameField);

            koreanPatch$animationUtil.init(x - 4, 0);
            koreanPatch$animationUtil.calculateAnimation(x, 0);

            Indicator.showIndicator(context, koreanPatch$animationUtil.getResultX(), y);
        } else {
            KoreanPatchClient.bypassInjection = true;
        }
    }
}
