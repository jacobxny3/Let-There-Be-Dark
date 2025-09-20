package com.searchablemenus.mixin.client;

import com.searchablemenus.DarkMode;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenTintMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At("TAIL"))
    private void afterScreenRender(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!DarkMode.isEnabled()) return;

        int alpha = (int) (DarkMode.getDarkness() * 255) & 0xFF;
        int overlay = (alpha << 24); // ARGB black with adjustable alpha
        ctx.fill(0, 0, ctx.getScaledWindowWidth(), ctx.getScaledWindowHeight(), overlay);
    }
}
