package com.searchablemenus;

public final class DarkMode {
    private static boolean enabled = false;
    private static float darkness = 0.6f;

    // Default title screen button position (modifiable via config)
    private static int buttonX = -135;  // relative from right edge
    private static int buttonY = 163;
    private static int buttonW = 100;
    private static int buttonH = 20;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void toggle() {
        enabled = !enabled;
    }

    public static void set(boolean value) {
        enabled = value;
    }

    public static float getDarkness() {
        return darkness;
    }

    public static void setDarkness(float value) {
        darkness = Math.max(0f, Math.min(1f, value));
    }

    // Button position getters/setters
    public static int getButtonX() { return buttonX; }
    public static int getButtonY() { return buttonY; }
    public static int getButtonW() { return buttonW; }
    public static int getButtonH() { return buttonH; }

    public static void setButtonX(int x) { buttonX = x; }
    public static void setButtonY(int y) { buttonY = y; }
    public static void setButtonW(int w) { buttonW = w; }
    public static void setButtonH(int h) { buttonH = h; }
}
