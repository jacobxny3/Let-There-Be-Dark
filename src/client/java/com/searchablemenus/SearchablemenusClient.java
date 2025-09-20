package com.searchablemenus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class SearchablemenusClient implements ClientModInitializer {

    private static KeyBinding toggleDarkMode;
    private static final Gson GSON = new Gson();
    private static int storedKeyCode = GLFW.GLFW_KEY_F10; // default key

    @Override
    public void onInitializeClient() {
        loadConfig();

        toggleDarkMode = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Dark Mode",
                InputUtil.Type.KEYSYM,
                storedKeyCode, // use stored key
                "key.categories.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleDarkMode.wasPressed()) {
                DarkMode.toggle();
                saveConfig();
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> saveConfig());
    }

    private static Path configPath() {
        return MinecraftClient.getInstance().runDirectory.toPath().resolve("config/searchablemenus.json");
    }

    public static void loadConfig() {
        Path p = configPath();
        if (!Files.exists(p)) return;
        try (Reader r = Files.newBufferedReader(p)) {
            JsonObject o = GSON.fromJson(r, JsonObject.class);
            if (o != null) {
                if (o.has("darkMode")) DarkMode.set(o.get("darkMode").getAsBoolean());
                if (o.has("darkness")) DarkMode.setDarkness(o.get("darkness").getAsFloat());
                if (o.has("buttonX")) DarkMode.setButtonX(o.get("buttonX").getAsInt());
                if (o.has("buttonY")) DarkMode.setButtonY(o.get("buttonY").getAsInt());
                if (o.has("buttonW")) DarkMode.setButtonW(o.get("buttonW").getAsInt());
                if (o.has("buttonH")) DarkMode.setButtonH(o.get("buttonH").getAsInt());
                if (o.has("toggleKey")) storedKeyCode = o.get("toggleKey").getAsInt(); // ✅ store loaded key
            }
        } catch (IOException ignored) {}
    }

    public static void saveConfig() {
        Path p = configPath();
        try {
            Files.createDirectories(p.getParent());
            JsonObject o = new JsonObject();
            o.addProperty("darkMode", DarkMode.isEnabled());
            o.addProperty("darkness", DarkMode.getDarkness());
            o.addProperty("buttonX", DarkMode.getButtonX());
            o.addProperty("buttonY", DarkMode.getButtonY());
            o.addProperty("buttonW", DarkMode.getButtonW());
            o.addProperty("buttonH", DarkMode.getButtonH());

            // ✅ Save stored key code
            o.addProperty("toggleKey", storedKeyCode);

            try (Writer w = Files.newBufferedWriter(p)) {
                GSON.toJson(o, w);
            }
        } catch (IOException ignored) {}
    }

    // Used by Mod Menu integration
    public static void saveConfigStatic() {
        saveConfig();
    }

    // Called from config screen when key changes
    public static void updateKeybind(InputUtil.Key newKey) {
        storedKeyCode = newKey.getCode();
        toggleDarkMode.setBoundKey(newKey);
        KeyBinding.updateKeysByCode();
        saveConfig();
    }

    public static KeyBinding getToggleKeybind() {
        return toggleDarkMode;
    }
}
