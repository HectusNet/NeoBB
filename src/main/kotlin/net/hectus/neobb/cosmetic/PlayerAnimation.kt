package net.hectus.neobb.cosmetic

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Utilities
import net.hectus.neobb.util.asLocation
import net.hectus.neobb.util.bukkitRunTimer
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

enum class PlayerAnimation(private val action: (Player, Cord) -> Unit) {
    NONE({ _, _ -> }),
    LIGHTNING({ p, _ -> p.world.strikeLightningEffect(p.location) }),
    ANVIL({ p, _ -> p.location.clone().add(0.0, 5.0, 0.0).block.setType(org.bukkit.Material.ANVIL, true) }),
    EXPLODED({ p, _ -> p.location.createExplosion(p, 0.0f, false, false) }),
    FIREWORK({ p, _ -> p.world.spawn(p.location, Firework::class.java) { f ->
        f.ticksToDetonate = 0
        val meta = f.fireworkMeta
        meta.addEffect(FireworkEffect.builder()
            .withColor(Color.fromRGB(Random.nextInt(0x000000, 0xFFFFFF)))
            .build())
        f.fireworkMeta = meta
    } }),
    FROST({ _, _ -> }), // TODO: Cool frost effect going around map!
    MOB_RAIN({ p, c ->
        val mobsLeft = AtomicInteger(10)
        bukkitRunTimer(0, 5) { r ->
            if (mobsLeft.decrementAndGet() <= 0) r.cancel()
            p.world.spawnEntity(c.add(Cord(Random.nextDouble(9.0), 5.0, Random.nextDouble(9.0))).asLocation(p.world), Randomizer.fromCollection(Utilities.ENTITY_TYPES))
        }
    });

    fun play(player: NeoPlayer) {
        play(player.player, player.game.warp.lowCorner)
    }

    fun play(player: Player, cord: Cord) {
        action.invoke(player, cord)
    }
}
