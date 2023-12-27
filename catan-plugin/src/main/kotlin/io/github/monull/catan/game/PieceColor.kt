package io.github.monull.catan.game

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material

enum class PieceColor(val group: PieceGroup, val textColor: NamedTextColor, val material: Material) {
    RED(PieceGroup.RED, NamedTextColor.RED, Material.PINK_DYE),
    GOLD(PieceGroup.YELLOW, NamedTextColor.GOLD, Material.ORANGE_DYE),
    GREEN(PieceGroup.GREEN, NamedTextColor.GREEN, Material.LIME_DYE),
    AQUA(PieceGroup.AQUA, NamedTextColor.AQUA, Material.LIGHT_BLUE_DYE);

    val color = Color.fromRGB(textColor.value())

    companion object {
        fun textColorOf(textColor: NamedTextColor): PieceColor? {
            return values().find { it.textColor == textColor }
        }
    }
}

enum class PieceGroup(val barColor: BossBar.Color) {
    RED(BossBar.Color.RED),
    YELLOW(BossBar.Color.YELLOW),
    GREEN(BossBar.Color.GREEN),
    AQUA(BossBar.Color.BLUE)
}