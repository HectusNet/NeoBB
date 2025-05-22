package net.hectus.util.display

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

open class SimpleActionBar(
    player: Player, updateInterval: Long,
    protected val text: () -> Component
) : SimpleDisplay(player, updateInterval) {
    override fun setup() {}

    override fun update() {
        player.sendActionBar(text())
    }

    override fun remove() {
        player.sendActionBar(Component.empty())
    }
}

open class GradientActionBar(
    player: Player, updateInterval: Long,
    protected val colors: List<TextColor>,
    protected val speed: Double = 0.1,
    text: () -> Component,
) : SimpleActionBar(player, updateInterval, text) {
    private val colorText = colors.joinToString(":") { it.asHexString() }

    private var phase = 0.0

    override fun update() {
        phase += speed
        if (phase >= 1.0)
            phase -= 2.0

        player.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:$colorText:$phase><text></gradient>", Placeholder.component("text", text())))
    }
}
