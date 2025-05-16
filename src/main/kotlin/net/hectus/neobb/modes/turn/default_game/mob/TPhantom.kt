package net.hectus.neobb.modes.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.entity.Phantom

class TPhantom(data: Phantom?, cord: Cord?, player: NeoPlayer?) : MobTurn<Phantom>(data, cord, player), AttackFunction, SupernaturalClazz {
    override val cost: Int = 5
}
