package net.hectus.neobb.modes.turn.default_game.other

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer

abstract class OtherTurn<T>(data: T?, cord: Cord?, player: NeoPlayer?) : Turn<T>(data, cord, player)
