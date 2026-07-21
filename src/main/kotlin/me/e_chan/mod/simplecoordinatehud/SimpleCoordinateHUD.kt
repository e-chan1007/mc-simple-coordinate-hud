package me.e_chan.mod.simplecoordinatehud

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.Identifier
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory


object SimpleCoordinateHUD : ClientModInitializer {
    private val client: Minecraft by lazy { Minecraft.getInstance() }
    internal val LOGGER = LoggerFactory.getLogger(SimpleCoordinateHUD::class.java)
    private lateinit var configKeyBinding: KeyMapping

    override fun onInitializeClient() {
        // 設定を読み込み
        ModConfig.load()

        configKeyBinding = KeyMappingHelper.registerKeyMapping(KeyMapping("key.simplecoordinatehud.config", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_SEMICOLON,
            KeyMapping.Category.GAMEPLAY))

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: Minecraft ->
            if (configKeyBinding.isDown) {
                val configScreen = client.gui.screen()?.let { ModConfig.createScreen(it) }
                client.gui.setScreen(configScreen)
            }
        })

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("simplecoordinatehud", "coordinate"), run { HUDRender(client) })

	}
}
