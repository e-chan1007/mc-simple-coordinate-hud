package me.e_chan.mod.simplecoordinatehud

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.matrixcreations.libraries.MatrixColorAPI
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.resources.language.I18n
import net.minecraft.world.phys.BlockHitResult

class HUDRender(private val client: Minecraft): HudElement {
    internal val WHITE = 0xFFFFFFFF.toInt()
    override fun extractRenderState(graphics: GuiGraphicsExtractor, deltaTracker: DeltaTracker) {
        if (client.debugOverlay.showDebugScreen()) return;
        val player = client.player ?: return

        val xOffset = 5
        var yOffset = 5

        if (ModConfig.shouldShowPlayerCoordinate) {
            var posText: String
            try {
                posText =
                    String.format(ModConfig.playerCoordinateFormat, player.x, player.y, player.z)
            } catch (e: Exception) {
                posText = I18n.get("simplecoordinatehud.formatError")
            }
            graphics.text(client.font, MatrixColorAPI.process(posText), xOffset, yOffset, WHITE)
            yOffset += 12
        }

        if (ModConfig.shouldShowFacing) {
            var facingText: String
            try {
                facingText = String.format(ModConfig.facingFormat, getFacing(player.yRot))
            } catch (e: Exception) {
                facingText = I18n.get("simplecoordinatehud.formatError")
            }
            graphics.text(client.font, MatrixColorAPI.process(facingText), xOffset, yOffset, WHITE)
            yOffset += 12
        }

        if (ModConfig.shouldShowTargetBlockCoordinate) {
            val target = client.hitResult
            if (target is BlockHitResult) {
                val pos = target.blockPos
                val blockState = client.level?.getBlockState(pos)
                blockState?.isAir?.let {
                    if (!it) {
                        val blockName = blockState.block.name.string

                        var blockPosText: String
                        try {
                            blockPosText = String.format(
                                ModConfig.targetBlockCoordinateFormat,
                                pos.x,
                                pos.y,
                                pos.z,
                                blockName
                            )
                        } catch (e: Exception) {
                            blockPosText = I18n.get("simplecoordinatehud.formatError")
                        }
                        graphics.text(client.font, MatrixColorAPI.process(blockPosText), xOffset, yOffset, WHITE)
                    }
                }
            }
        }
    }

    private fun getFacing(yaw: Float): String {
        val normalized = (yaw % 360 + 360 + 45) % 360
        return when {
            normalized < 90 -> ModConfig.facingSouth
            normalized < 180 -> ModConfig.facingWest
            normalized < 270 -> ModConfig.facingNorth
            else -> ModConfig.facingEast
        }
    }
}
