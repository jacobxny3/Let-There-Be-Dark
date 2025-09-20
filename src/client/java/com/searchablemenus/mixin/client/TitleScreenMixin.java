package com.searchablemenus.mixin.client;

import com.searchablemenus.DarkMode;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addDarkModeToggle(CallbackInfo ci) {
        boolean essentialPresent = FabricLoader.getInstance().isModLoaded("essential");

        int x = this.width + DarkMode.getButtonX(); // X is relative to right edge
        int y = DarkMode.getButtonY();
        int w = DarkMode.getButtonW();
        int h = DarkMode.getButtonH();

        // Adjust for Essential if present (optional default offset)
        if (essentialPresent) {
            y -= 90;
            x += 47;
            w -= 20;
        }

        Text label = Text.literal(DarkMode.isEnabled() ? "Dark Mode: ON" : "Dark Mode: OFF");
        this.addDrawableChild(ButtonWidget.builder(
                label,
                button -> {
                    DarkMode.toggle();
                    button.setMessage(Text.literal(DarkMode.isEnabled() ? "Dark Mode: ON" : "Dark Mode: OFF"));
                }
        ).dimensions(x, y, w, h).build());
    }
}
