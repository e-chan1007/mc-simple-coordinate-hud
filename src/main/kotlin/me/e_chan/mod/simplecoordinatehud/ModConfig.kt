package me.e_chan.mod.simplecoordinatehud

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.locale.Language
import net.minecraft.network.chat.Component
import java.io.File
import java.nio.file.Files

object ModConfig {
    var shouldShowPlayerCoordinate: Boolean = true
    var shouldShowFacing: Boolean = true
    var shouldShowTargetBlockCoordinate: Boolean = true

    var playerCoordinateFormat: String = ""
    var facingFormat: String = ""
    var facingSouth: String = ""
    var facingWest: String = ""
    var facingNorth: String = ""
    var facingEast: String = ""
    var targetBlockCoordinateFormat: String = ""

    private val configFile = File("config/simplecoordinatehud.json")
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    private const val KEY_PLAYER_COORDINATE_FORMAT = "simplecoordinatehud.defaultFormat.playerCoordinate"
    private const val KEY_FACING_FORMAT = "simplecoordinatehud.defaultFormat.facing"
    private const val KEY_FACING_SOUTH = "simplecoordinatehud.defaultFormat.facing.south"
    private const val KEY_FACING_WEST = "simplecoordinatehud.defaultFormat.facing.west"
    private const val KEY_FACING_NORTH = "simplecoordinatehud.defaultFormat.facing.north"
    private const val KEY_FACING_EAST = "simplecoordinatehud.defaultFormat.facing.east"
    private const val KEY_BLOCK_COORDINATE_FORMAT = "simplecoordinatehud.defaultFormat.blockCoordinate"

    private fun getDefaultPlayerCoordinateFormat() = Language.getInstance().getOrDefault(KEY_PLAYER_COORDINATE_FORMAT).replace("@", "%")
    private fun getDefaultFacingFormat() = Language.getInstance().getOrDefault(KEY_FACING_FORMAT).replace("@", "%")
    private fun getDefaultFacingSouth() = Language.getInstance().getOrDefault(KEY_FACING_SOUTH).replace("@", "%")
    private fun getDefaultFacingWest() = Language.getInstance().getOrDefault(KEY_FACING_WEST).replace("@", "%")
    private fun getDefaultFacingNorth() = Language.getInstance().getOrDefault(KEY_FACING_NORTH).replace("@", "%")
    private fun getDefaultFacingEast() = Language.getInstance().getOrDefault(KEY_FACING_EAST).replace("@", "%")
    private fun getDefaultTargetBlockCoordinateFormat() = Language.getInstance().getOrDefault(KEY_BLOCK_COORDINATE_FORMAT).replace("@", "%")

    fun load() {
        if (!configFile.exists()) {
            setDefaults()
            save()
            return
        }

        try {
            val json = Files.readString(configFile.toPath())
            val config = gson.fromJson(json, ConfigData::class.java)

            shouldShowPlayerCoordinate = config.shouldShowPlayerCoordinate
            shouldShowFacing = config.shouldShowFacing
            shouldShowTargetBlockCoordinate = config.shouldShowTargetBlockCoordinate
            playerCoordinateFormat = config.playerCoordinateFormat
            facingFormat = config.facingFormat
            facingSouth = config.facingSouth
            facingWest = config.facingWest
            facingNorth = config.facingNorth
            facingEast = config.facingEast
            targetBlockCoordinateFormat = config.targetBlockCoordinateFormat
        } catch (e: Exception) {
            SimpleCoordinateHUD.LOGGER.error("Failed to load config", e)
            setDefaults()
            save()
        }
    }

    fun save() {
        try {
            val config = ConfigData(
                shouldShowPlayerCoordinate,
                shouldShowFacing,
                shouldShowTargetBlockCoordinate,
                playerCoordinateFormat,
                facingFormat,
                facingSouth,
                facingWest,
                facingNorth,
                facingEast,
                targetBlockCoordinateFormat
            )
            val json = gson.toJson(config)
            Files.writeString(configFile.toPath(), json)
        } catch (e: Exception) {
            SimpleCoordinateHUD.LOGGER.error("Failed to save config", e)
        }
    }

    private fun setDefaults() {
        shouldShowPlayerCoordinate = true
        shouldShowFacing = true
        shouldShowTargetBlockCoordinate = true
        playerCoordinateFormat = getDefaultPlayerCoordinateFormat()
        facingFormat = getDefaultFacingFormat()
        facingSouth = getDefaultFacingSouth()
        facingWest = getDefaultFacingWest()
        facingNorth = getDefaultFacingNorth()
        facingEast = getDefaultFacingEast()
        targetBlockCoordinateFormat = getDefaultTargetBlockCoordinateFormat()
    }

    fun createScreen(parent: Screen): Screen {
        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Simple Coordinate HUD"))
            .category(createToggleCategory())
            .category(createFormatCategory())
            .save { save() }
            .build()
            .generateScreen(parent)
    }

    private fun createToggleCategory(): ConfigCategory {
        return ConfigCategory.createBuilder()
            .name(Component.translatable("text.autoconfig.simplecoordinatehud.category.0_toggle"))
            .option(Option.createBuilder<Boolean>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.shouldShowPlayerCoordinate"))
                .binding(
                    true,
                    { shouldShowPlayerCoordinate },
                    { value -> shouldShowPlayerCoordinate = value }
                )
                .controller(BooleanControllerBuilder::create)
                .build())
            .option(Option.createBuilder<Boolean>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.shouldShowFacing"))
                .binding(
                    true,
                    { shouldShowFacing },
                    { value -> shouldShowFacing = value }
                )
                .controller(BooleanControllerBuilder::create)
                .build())
            .option(Option.createBuilder<Boolean>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.shouldShowTargetBlockCoordinate"))
                .binding(
                    true,
                    { shouldShowTargetBlockCoordinate },
                    { value -> shouldShowTargetBlockCoordinate = value }
                )
                .controller(BooleanControllerBuilder::create)
                .build())
            .build()
    }

    private fun createFormatCategory(): ConfigCategory {
        return ConfigCategory.createBuilder()
            .name(Component.translatable("text.autoconfig.simplecoordinatehud.category.1_format"))
            .option(Option.createBuilder<String>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.playerCoordinateFormat"))
                .description(OptionDescription.of(Component.translatableEscape("text.autoconfig.simplecoordinatehud.option.playerCoordinateFormat.description")))
                .binding(
                    getDefaultPlayerCoordinateFormat(),
                    { playerCoordinateFormat },
                    { value -> playerCoordinateFormat = value }
                )
                .controller(StringControllerBuilder::create)
                .build())
            .option(Option.createBuilder<String>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.facingFormat"))
                .description(OptionDescription.of(Component.translatableEscape("text.autoconfig.simplecoordinatehud.option.facingFormat.description")))
                .binding(
                    getDefaultFacingFormat(),
                    { facingFormat },
                    { value -> facingFormat = value }
                )
                .controller(StringControllerBuilder::create)
                .build())
            .option(Option.createBuilder<String>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.facingSouth"))
                .binding(
                    getDefaultFacingSouth(),
                    { facingSouth },
                    { value -> facingSouth = value }
                )
                .controller(StringControllerBuilder::create)
                .build())
            .option(Option.createBuilder<String>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.facingWest"))
                .binding(
                    getDefaultFacingWest(),
                    { facingWest },
                    { value -> facingWest = value }
                )
                .controller(StringControllerBuilder::create)
                .build())
            .option(Option.createBuilder<String>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.facingNorth"))
                .binding(
                    getDefaultFacingNorth(),
                    { facingNorth },
                    { value -> facingNorth = value }
                )
                .controller(StringControllerBuilder::create)
                .build())
            .option(Option.createBuilder<String>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.facingEast"))
                .binding(
                    getDefaultFacingEast(),
                    { facingEast },
                    { value -> facingEast = value }
                )
                .controller(StringControllerBuilder::create)
                .build())
            .option(Option.createBuilder<String>()
                .name(Component.translatable("text.autoconfig.simplecoordinatehud.option.targetBlockCoordinateFormat"))
                .description(OptionDescription.of(Component.translatableEscape("text.autoconfig.simplecoordinatehud.option.targetBlockCoordinateFormat.description")))
                .binding(
                    getDefaultTargetBlockCoordinateFormat(),
                    { targetBlockCoordinateFormat },
                    { value -> targetBlockCoordinateFormat = value }
                )
                .controller(StringControllerBuilder::create)
                .build())
            .build()
    }

    private data class ConfigData(
        val shouldShowPlayerCoordinate: Boolean = true,
        val shouldShowFacing: Boolean = true,
        val shouldShowTargetBlockCoordinate: Boolean = true,
        val playerCoordinateFormat: String = "",
        val facingFormat: String = "",
        val facingSouth: String = "",
        val facingWest: String = "",
        val facingNorth: String = "",
        val facingEast: String = "",
        val targetBlockCoordinateFormat: String = ""
    )
}
