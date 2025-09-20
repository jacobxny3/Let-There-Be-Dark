package com.searchablemenus.config;

import com.searchablemenus.DarkMode;
import com.searchablemenus.SearchablemenusClient;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class SearchableMenusConfigScreen {

    public static Screen build(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Let There Be Dark Settings"))
                .setSavingRunnable(SearchablemenusClient::saveConfigStatic);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // ----- GENERAL CATEGORY -----
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Dark Mode"), DarkMode.isEnabled())
                .setSaveConsumer(DarkMode::set)
                .build());

        general.addEntry(entryBuilder.startFloatField(Text.literal("Darkness Amount"), DarkMode.getDarkness())
                .setMin(0f)
                .setMax(1f)
                .setTooltip(Text.literal("0.0 = no overlay, 1.0 = fully black"))
                .setSaveConsumer(DarkMode::setDarkness)
                .build());

        // Keybind picker (keep it in General section)
        KeyBinding keybind = SearchablemenusClient.getToggleKeybind();
        general.addEntry(entryBuilder.startKeyCodeField(Text.literal("Toggle Keybind"), keybind.getDefaultKey())
                .setDefaultValue(keybind.getDefaultKey())
                .setKeySaveConsumer((InputUtil.Key newKey) -> {
                    // Update keybind and save immediately
                    SearchablemenusClient.updateKeybind(newKey);
                })
                .build());

        // ----- TITLE SCREEN BUTTON CATEGORY -----
        ConfigCategory buttonSettings = builder.getOrCreateCategory(Text.literal("Title Screen Button"));

        buttonSettings.addEntry(entryBuilder.startIntField(Text.literal("Button X Offset (from right edge)"), DarkMode.getButtonX())
                .setTooltip(Text.literal("Negative = move left, Positive = move right"))
                .setSaveConsumer(DarkMode::setButtonX)
                .build());

        buttonSettings.addEntry(entryBuilder.startIntField(Text.literal("Button Y Position"), DarkMode.getButtonY())
                .setTooltip(Text.literal("Negative = move up, Positive = move down"))
                .setSaveConsumer(DarkMode::setButtonY)
                .build());

        buttonSettings.addEntry(entryBuilder.startIntField(Text.literal("Button Width"), DarkMode.getButtonW())
                .setSaveConsumer(DarkMode::setButtonW)
                .build());

        buttonSettings.addEntry(entryBuilder.startIntField(Text.literal("Button Height"), DarkMode.getButtonH())
                .setSaveConsumer(DarkMode::setButtonH)
                .build());

        buttonSettings.addEntry(entryBuilder.startTextDescription(Text.literal("Reset to Defaults: Click button below")).build());
        buttonSettings.addEntry(entryBuilder.startBooleanToggle(Text.literal("Reset Button Position"), false)
                .setSaveConsumer(reset -> {
                    if (reset) {
                        DarkMode.setButtonX(-135);
                        DarkMode.setButtonY(163);
                        DarkMode.setButtonW(100);
                        DarkMode.setButtonH(20);
                    }
                })
                .build());


        return builder.build();
    }
}
