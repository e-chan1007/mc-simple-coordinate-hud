package me.e_chan.mod.simplecoordinatehud

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory


object SimpleCoordinateHUD : ClientModInitializer {
    private val client: MinecraftClient by lazy { MinecraftClient.getInstance() }
    internal val CONFIG: ModConfig by lazy { AutoConfig.getConfigHolder(ModConfig::class.java).getConfig() }
    internal val LOGGER = LoggerFactory.getLogger(SimpleCoordinateHUD::class.java)
    private lateinit var configKeyBinding: KeyBinding

    override fun onInitializeClient() {
        AutoConfig.register(ModConfig::class.java, { def, cls -> Toml4jConfigSerializer(def, cls) })

        configKeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding("key.simplecoordinatehud.config", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_SEMICOLON, "category.simplecoordinatehud"))

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
            if (configKeyBinding.wasPressed()) {
                val configScreen = AutoConfig.getConfigScreen(ModConfig::class.java, client.currentScreen).get()
                client.setScreen(configScreen)
            }
        })

        HudRenderCallback.EVENT.register(HUDRender(client))

	}
}