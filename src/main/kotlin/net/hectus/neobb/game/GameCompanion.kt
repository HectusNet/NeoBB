package net.hectus.neobb.game

import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import org.bukkit.World
import org.bukkit.entity.Player

interface GameCompanion<T : Game> {
    val gameConstructor: (World, List<Player>, GameDifficulty) -> T

    val gameInfo: GameInfo
}
