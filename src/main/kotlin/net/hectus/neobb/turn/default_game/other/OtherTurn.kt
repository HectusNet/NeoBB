package net.hectus.neobb.turn.default_game.other

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn

abstract class OtherTurn<T>(data: T?, cord: Cord?, player: NeoPlayer?) : Turn<T>(data, cord, player)