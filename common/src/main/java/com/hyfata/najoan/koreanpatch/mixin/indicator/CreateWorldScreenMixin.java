package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.client.GUIStatus;
import com.hyfata.najoan.koreanpatch.mixin.accessor.CreateWorldScreenGameTabAccessor;
import com.hyfata.najoan.koreanpatch.mixin.accessor.TabNavigationBarAccessor;
import com.hyfata.najoan.koreanpatch.util.minecraft.EditBoxUtil;
import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
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
    private final AnimationHandler koreanPatch$animationHandler = new AnimationHandler();

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TabNavigationBarAccessor tabInvoker = (TabNavigationBarAccessor) tabNavigationBar;
        Tab currentTab = tabInvoker.getTabManager().getCurrentTab();

        if (currentTab instanceof CreateWorldScreen.GameTab) {
            GUIStatus.getInstance().setBypassInjection(false);
            CreateWorldScreenGameTabAccessor gameTabAccessor = (CreateWorldScreenGameTabAccessor) currentTab;
            EditBox worldNameField = gameTabAccessor.getNameEdit();
            Component text = Component.translatable("selectWorld.enterName");

            float x = EditBoxUtil.getCursorXWithText(worldNameField, text, worldNameField.getX()) + 4;
            float y = EditBoxUtil.calculateIndicatorY(worldNameField);

            koreanPatch$animationHandler.init(x - 4, 0);
            koreanPatch$animationHandler.calculateAnimation(x, 0);

            IndicatorHandler.showIndicator(context, koreanPatch$animationHandler.getResultX(), y);
        } else {
            GUIStatus.getInstance().setBypassInjection(true);
        }
    }
}
