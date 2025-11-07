package net.hectus.neobb.game.mode

import com.destroystokyo.paper.ParticleBuilder
import com.marcpg.libpg.display.*
import com.marcpg.libpg.util.component
import net.hectus.neobb.NeoBB
import net.hectus.neobb.external.rating.Rank
import net.hectus.neobb.external.rating.Rank.Companion.toRankTranslations
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.GameCompanion
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.modes.lore.DummyItemLoreBuilder
import net.hectus.neobb.modes.shop.RandomizedShop
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Constants
import net.hectus.neobb.util.Ticking
import net.hectus.neobb.util.Utilities
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.ChatFormatting
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.PlayerTeam
import net.minecraft.world.scores.Scoreboard
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.time.LocalDateTime
import kotlin.experimental.and
import kotlin.experimental.or

class PvpGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = Constants.DEFAULT_DIFFICULTY) : Game(world, bukkitPlayers, difficulty) {
    companion object : GameCompanion<PvpGame> {
        override val gameConstructor: (World, List<Player>, GameDifficulty) -> PvpGame =
            { w, p, d -> PvpGame(w, p, d) }

        override val gameInfo: GameInfo = GameInfo(
            namespace = "pvp",
            deckSize = 36, // Exactly one full player inventory, excluding armor and offhand slots.
            shop = RandomizedShop::class,
            loreBuilder = DummyItemLoreBuilder::class,
        )
    }

    override val info: GameInfo = gameInfo

    override val scoreboard: (NeoPlayer) -> SimpleScoreboard = { p -> SimpleScoreboard(
        p,
        3,
        MiniMessage.miniMessage()
            .deserialize("<gradient:#D068FF:#EC1A3D>NeoBB<reset><#999999> - <gradient:#AA4949:#FF4949>Combat"),
        ValueScoreboardEntry(
            p.locale().component("scoreboard.alive", color = Colors.ACCENT)
        ) { component("${p.game.players.size}/${p.game.initialPlayers.size}") },
        ValueScoreboardEntry(
            p.locale().component("scoreboard.time", color = Colors.ACCENT)
        ) { component(p.game.timeLeft.preciselyFormatted) },
        ValueScoreboardEntry(p.locale().component("scoreboard.luck", color = Colors.ACCENT)) { Component.text(p.luck) },
        BlankScoreboardEntry(),
        ValueScoreboardEntry(
            p.locale().component("scoreboard.rank", color = Colors.ACCENT)
        ) { Rank.ofElo(p.databaseInfo.elo).toRankTranslations(p.locale()) },
        ValueScoreboardEntry(
            p.locale().component("scoreboard.elo", color = Colors.ACCENT)
        ) { Component.text(p.databaseInfo.elo.toInt()) },
        BlankScoreboardEntry(),
        SimpleScoreboardEntry {
            component(
                "NeoBB-${NeoBB.VERSION} (d${Integer.toHexString(LocalDateTime.now().dayOfYear)}h${LocalDateTime.now().hour})",
                Colors.EXTRA
            )
        },
        StaticScoreboardEntry(component("mc.hectus.net", Colors.LINK)),
    ) }

    override val actionBar: (NeoPlayer) -> SimpleActionBar = { p -> SimpleActionBar(p, 1) {
        val focus = p.targetPlayerOrNull()
        if (focus == null) {
            it.component("actionbar.focus.none", color = Colors.EXTRA)
        } else {
            it.component("actionbar.focus.player", "", color = Colors.SECONDARY).append(component(focus.name(), color = Colors.ACCENT))
        }
    } }

    val highlighted = mutableMapOf<NeoPlayer, NeoPlayer>()

    override fun extraTick(tick: Ticking.Tick) {
        players.forEach {
            if (it in highlighted) {
                highlighted[it]!!.player.setGlowing(it.player, false)
                highlighted -= it
            }

            val target = it.targetPlayerOrNull(beam = true)
            if (target != null) {
                highlighted[it] = target
                target.player.setGlowing(it.player, true, ChatFormatting.DARK_RED)
            }
        }
    }

    override fun targetPlayer(player: NeoPlayer, beam: Boolean): NeoPlayer? {
        val eyeLocation = player.player.eyeLocation.clone().apply { pitch /= 3 }
        val direction = eyeLocation.direction

        if (beam) {
            Utilities.generateBeam(
                start = eyeLocation.subtract(0.0, 1.2, 0.0),
                direction = direction,
                length = (Constants.TARGET_MAX_RANGE).toInt(),
                spacing = 0.25,
                ParticleBuilder(Particle.DUST)
                    .color(Color.YELLOW))
        }

        val result = eyeLocation.world.rayTraceEntities(eyeLocation, direction, Constants.TARGET_MAX_RANGE, Constants.TARGET_TOLERANCE) { p -> p is Player && player.uuid() != p.uniqueId }
        return if (result?.hitEntity == null) null else player(result.hitEntity as Player)
    }

    private fun Entity.setGlowing(viewingPlayer: Player, glow: Boolean, glowColor: ChatFormatting? = null) {
        val nmsEntity = (this as CraftEntity).handle
        val entityId = nmsEntity.id
        val entityName = when (nmsEntity) {
            is ServerPlayer -> nmsEntity.gameProfile.name
            else -> nmsEntity.stringUUID
        }

        val dataWatcher = nmsEntity.entityData
        val bitmaskAccessor = EntityDataAccessor(0, EntityDataSerializers.BYTE)

        val oldBitMask = dataWatcher.get(bitmaskAccessor)
        val newBitMask = if (glow) {
            oldBitMask or 0x40.toByte()
        } else {
            oldBitMask and 0xBF.toByte()
        }

        dataWatcher.set(bitmaskAccessor, newBitMask)

        val conn = (viewingPlayer as CraftPlayer).handle.connection
        dataWatcher.packDirty()?.let {
            val packet = ClientboundSetEntityDataPacket(entityId, it)
            conn.send(packet)
        }

        val teamName = "glow_${entityName.take(12)}"

        if (glow && glowColor != null) {
            val scoreboard = Scoreboard()
            val team = PlayerTeam(scoreboard, teamName).apply {
                color = glowColor
                setSeeFriendlyInvisibles(false)
                isAllowFriendlyFire = false
                players.add(entityName)
            }
            val packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true)
            conn.send(packet)
        } else {
            val scoreboard = Scoreboard()
            val dummyTeam = PlayerTeam(scoreboard, teamName)
            val packet = ClientboundSetPlayerTeamPacket.createRemovePacket(dummyTeam)
            conn.send(packet)
        }
    }
}
