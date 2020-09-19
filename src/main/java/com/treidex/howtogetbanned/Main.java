package com.treidex.howtogetbanned;

import com.sun.jna.platform.win32.WinUser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.client.input.Input;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.io.File;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements ModInitializer {
    private static GameOptions optionsScreen;

    private static KeyBinding zoomBinding;
    private static double zoomFov = 15, defaultFov = 90;

    static {
        optionsScreen = new GameOptions(MinecraftClient.getInstance(), new File("main/resources/options.txt"));

        zoomBinding = new KeyBinding("key.howtogetbanned.zoom", InputUtil.Type.KEYSYM, GLFW_KEY_C, "category.howtogetbanned");
    }

    @Override
    public void onInitialize() {
        KeyBindingHelper.registerKeyBinding(zoomBinding);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (zoomBinding.isPressed())
                client.options.fov = zoomFov;
            else
                client.options.fov = defaultFov;
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;
            client.player.knockbackVelocity = 0;
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;
            if (client.options.keyJump.isPressed())
                client.player.setVelocity(client.player.getVelocity().x, 0.42F, client.player.getVelocity().z);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;
            client.player.setNoGravity(true);
            if (client.options.keyJump.isPressed())
                client.player.setVelocity(client.player.getVelocity().x, 0.42F, client.player.getVelocity().z);
            else if (client.options.keySneak.isPressed())
                client.player.setVelocity(client.player.getVelocity().x, -0.42F, client.player.getVelocity().z);
            else
                client.player.setVelocity(client.player.getVelocity().x, 0, client.player.getVelocity().z);
        });
    }
}
