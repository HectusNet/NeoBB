package net.hectus.neobb.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import org.bukkit.entity.Phantom

class TPhantom(data: Phantom?, cord: Cord?, player: NeoPlayer?) : MobTurn<Phantom>(data, cord, player), AttackFunction, SupernaturalClazz {
    override val cost: Int = 5
}
