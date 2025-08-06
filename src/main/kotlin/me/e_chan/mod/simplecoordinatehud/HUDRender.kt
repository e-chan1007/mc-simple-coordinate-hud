package me.e_chan.mod.simplecoordinatehud

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.matrixcreations.libraries.MatrixColorAPI
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.resource.language.I18n
import net.minecraft.util.hit.BlockHitResult

class HUDRender(private val client: MinecraftClient): HudRenderCallback {
    internal val WHITE = 0xFFFFFFFF.toInt()
    override fun onHudRender(context: DrawContext, tickDelta: Float) {
        if (client.debugHud.shouldShowDebugHud()) return
        val player = client.player ?: return

        val xOffset = 5
        var yOffset = 5

        if (SimpleCoordinateHUD.CONFIG.shouldShowPlayerCoordinate) {
            var posText: String
            try {
                posText =
                    String.format(SimpleCoordinateHUD.CONFIG.playerCoordinateFormat, player.x, player.y, player.z)
            } catch (e: Exception) {
                posText = I18n.translate("simplecoordinatehud.formatError")
            }
            context.drawTextWithShadow(client.textRenderer, MatrixColorAPI.process(posText), xOffset, yOffset, WHITE)
            yOffset += 12
        }

        if (SimpleCoordinateHUD.CONFIG.shouldShowFacing) {
            var facingText: String
            try {
                facingText = String.format(SimpleCoordinateHUD.CONFIG.facingFormat, getFacing(player.yaw))
            } catch (e: Exception) {
                facingText = I18n.translate("simplecoordinatehud.formatError")
            }
            context.drawTextWithShadow(client.textRenderer, MatrixColorAPI.process(facingText), xOffset, yOffset, WHITE)
            yOffset += 12
        }

        if (SimpleCoordinateHUD.CONFIG.shouldShowTargetBlockCoordinate) {
            val target = client.crosshairTarget
            if (target is BlockHitResult) {
                val pos = target.blockPos
                val blockState = client.world?.getBlockState(pos)
                blockState?.isAir?.let {
                    if (!it) {
                        val blockName = I18n.translate(blockState.block.translationKey)

                        var blockPosText: String
                        try {
                            blockPosText = String.format(
                                SimpleCoordinateHUD.CONFIG.targetBlockCoordinateFormat,
                                pos.x,
                                pos.y,
                                pos.z,
                                blockName
                            )
                        } catch (e: Exception) {
                            blockPosText = I18n.translate("simplecoordinatehud.formatError")
                        }
                        context.drawTextWithShadow(client.textRenderer, MatrixColorAPI.process(blockPosText), xOffset, yOffset, WHITE)
                    }
                }
            }
            yOffset += 12
        }
    }

    private fun getFacing(yaw: Float): String {
        val normalized = (yaw % 360 + 360 + 45) % 360
        return when {
            normalized < 90 -> SimpleCoordinateHUD.CONFIG.facingSouth
            normalized < 180 -> SimpleCoordinateHUD.CONFIG.facingWest
            normalized < 270 -> SimpleCoordinateHUD.CONFIG.facingNorth
            else -> SimpleCoordinateHUD.CONFIG.facingEast
        }
    }
}