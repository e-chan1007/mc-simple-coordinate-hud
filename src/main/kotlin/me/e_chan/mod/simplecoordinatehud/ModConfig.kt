package me.e_chan.mod.simplecoordinatehud

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import net.minecraft.client.resource.language.I18n

@Config(name = "simplecoordinatehud")
class ModConfig: ConfigData {
    internal class Category {
        companion object {
            const val TOGGLE: String = "0_toggle"
            const val FORMAT: String = "1_format"
        }
    }

    @ConfigEntry.Category(Category.TOGGLE) var shouldShowPlayerCoordinate: Boolean = true
    @ConfigEntry.Category(Category.TOGGLE) var shouldShowFacing: Boolean = true
    @ConfigEntry.Category(Category.TOGGLE) var shouldShowTargetBlockCoordinate: Boolean = true

    @ConfigEntry.Category(Category.FORMAT) @ConfigEntry.Gui.Tooltip var playerCoordinateFormat: String = I18n.translate("simplecoordinatehud.defaultFormat.playerCoordinate", "%1$.1f", "%2$.1f", "%3$.1f")
    @ConfigEntry.Category(Category.FORMAT) @ConfigEntry.Gui.Tooltip var facingFormat: String = I18n.translate("simplecoordinatehud.defaultFormat.facing", $$"%1$s")
    @ConfigEntry.Category(Category.FORMAT) var facingSouth: String = I18n.translate("simplecoordinatehud.defaultFormat.facing.south")
    @ConfigEntry.Category(Category.FORMAT) var facingWest: String = I18n.translate("simplecoordinatehud.defaultFormat.facing.west")
    @ConfigEntry.Category(Category.FORMAT) var facingNorth: String = I18n.translate("simplecoordinatehud.defaultFormat.facing.north")
    @ConfigEntry.Category(Category.FORMAT) var facingEast: String = I18n.translate("simplecoordinatehud.defaultFormat.facing.east")
    @ConfigEntry.Category(Category.FORMAT) @ConfigEntry.Gui.Tooltip var targetBlockCoordinateFormat: String = I18n.translate("simplecoordinatehud.defaultFormat.blockCoordinate", $$"%1$d", $$"%2$d", $$"%3$d", $$"%4$s")
}