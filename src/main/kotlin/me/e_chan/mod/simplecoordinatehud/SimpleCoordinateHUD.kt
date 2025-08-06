package me.e_chan.mod.simplecoordinatehud

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.resource.language.I18n
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.hit.BlockHitResult
import org.slf4j.LoggerFactory

object SimpleCoordinateHUD : ModInitializer {
    private val logger = LoggerFactory.getLogger("simplecoordinatehud")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

        HudRenderCallback.EVENT.register(HudRenderCallback { context: DrawContext, tickDelta: Float ->
            val client = MinecraftClient.getInstance()
            if (client.debugHud.shouldShowDebugHud()) return@HudRenderCallback
            val player = client.player ?: return@HudRenderCallback

            var yOffset = 5

            // 座標
            val posText = "XYZ: ${player.x.toInt()}, ${player.y.toInt()}, ${player.z.toInt()}"
            context.drawText(client.textRenderer, posText, 5, yOffset, 0xFFFFFFFF.toInt(), true)

            yOffset += 12

            // 方角
            context.drawText(client.textRenderer, "Facing: ${getFacingWithAxis(player.yaw)}", 5, yOffset, 0xFFFFFFFF.toInt(), true)
            yOffset += 12

            // 見ているブロック
            val target = client.crosshairTarget
            if (target is BlockHitResult) {
                val bp = target.blockPos
                val blockState = client.world?.getBlockState(bp)
                blockState?.isAir?.let {
                    if (!it) {
                        context.drawText(
                            client.textRenderer,
                            "Block: ${bp.x}, ${bp.y}, ${bp.z} (${I18n.translate(blockState.block.translationKey)})",
                            5,
                            yOffset,
                            0xFFFFFFFF.toInt(),
                            true
                        )
                    }
                }
            }

        })
	}

    private fun getFacingWithAxis(yaw: Float): String {
        val normalized = (yaw % 360 + 360 + 45) % 360
        return when {
            normalized < 90 -> "South (Z+)"
            normalized < 180 -> "West (X-)"
            normalized < 270 -> "North (Z-)"
            else -> "East (X+)"
        }
    }
}