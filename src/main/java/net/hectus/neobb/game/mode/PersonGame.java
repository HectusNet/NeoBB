package net.hectus.neobb.game.mode;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.lore.PersonItemLoreBuilder;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.PersonShop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.warp.TDefaultWarp;
import net.hectus.neobb.turn.person_game.block.*;
import net.hectus.neobb.turn.person_game.categorization.*;
import net.hectus.neobb.turn.person_game.item.PTSuspiciousStew;
import net.hectus.neobb.turn.person_game.other.PTArmorStand;
import net.hectus.neobb.turn.person_game.other.PTPainting;
import net.hectus.neobb.turn.person_game.structure.*;
import net.hectus.neobb.turn.person_game.throwable.PTSnowball;
import net.hectus.neobb.turn.person_game.throwable.PTSplashPotion;
import net.hectus.neobb.turn.person_game.warp.*;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Modifiers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * Source: <a href="https://docs.google.com/document/d/1SOp-fDTZqx2l3XJoT0zqf4CDuTNCffU2pTejKrzEGO4">Semi-Official Google Document</a>
 */
public class PersonGame extends Game {
    public static final GameInfo INFO = new GameInfo(true, true, 10.0, 0, 9, new Time(10, Time.Unit.MINUTES), 10, PersonShop.class, PersonItemLoreBuilder.class, List.of(
            PTLightBlueCarpet.class, PTRedWool.class, PTBeeNest.class, PTCherryPressurePlate.class, PTLever.class,
            PTDaylightDetector.class, PTDiamondBlock.class, PTPinkCarpet.class, PTBirchLog.class, PTCake.class,
            PTBambooButton.class, PTOrangeWool.class, PTBrainCoral.class, PTGlowstone.class, PTRedStainedGlass.class,
            PTCherryButton.class, PTBlueConcrete.class, PTGreenCarpet.class, PTIronTrapdoor.class, PTBarrel.class,
            PTGrayWool.class, PTBrownStainedGlass.class, PTGoldBlock.class, PTVerdantFroglight.class, PTFletchingTable.class,
            PTHoneyBlock.class, PTWhiteStainedGlass.class, PTBlueIce.class, PTBlueStainedGlass.class, PTDripstone.class,
            PTNoteBlock.class, PTBlackCarpet.class, PTFenceGate.class, PTAmethystBlock.class, PTWhiteWool.class,
            PTStonecutter.class, PTSeaLantern.class, PTPurpleWool.class, PTSuspiciousStew.class, PTArmorStand.class,
            PTPainting.class, PTCandleCircle.class, PTPumpkinWall.class, PTStoneWall.class, PTTorchCircle.class,
            PTTurtling.class, PTWoodWall.class, PTSnowball.class, PTSplashPotion.class, PTAmethystWarp.class, PTFireWarp.class,
            PTIceWarp.class, PTSnowWarp.class, PTVillagerWarp.class, PTVoidWarp.class
    ));

    public PersonGame(boolean ranked, World world, @NotNull List<Player> players) {
        super(ranked, world, players, new TDefaultWarp(world));
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public @Nullable List<Component> scoreboard(NeoPlayer player) {
        Locale l = player.locale();
        try {
            return List.of(
                    MiniMessage.miniMessage().deserialize("<bold><#328825>Block <#37BF1F>Battles <reset><#9D9D9D>Alpha " + NeoBB.VERSION),
                    Component.empty(),
                    Translation.component(l, "scoreboard.stats").color(Colors.PERSON_2).decorate(TextDecoration.BOLD),
                    Component.text("💎", Colors.PERSON_4).append(Translation.component(l, "scoreboard.rank").color(Colors.PERSON_0)).append(Component.text("???")),
                    Component.text("⚔", Colors.PERSON_4).append(Translation.component(l, "scoreboard.elo").color(Colors.PERSON_0)).append(Component.text(player.elo(), Colors.PERSON_3)),
                    Component.empty(),
                    Component.empty(),
                    Component.text("mc", Colors.PERSON_1).append(Component.text(".hectus", Colors.PERSON_2)).append(Component.text(".net", Colors.PERSON_3))
            );
        } catch (Exception e) {
            return List.of(MiniMessage.miniMessage().deserialize("<bold><#328825>Block <#37BF1F>Battles <reset><#9D9D9D>Alpha " + NeoBB.VERSION));
        }
    }

    @Override
    public boolean preTurn(Turn<?> turn) {
        if (turn instanceof WinConCategory && turn.player().game.hasModifier(Modifiers.G_PERSON_NO_WIN_CONS)) {
            return true;
        }
        return super.preTurn(turn);
    }

    @Override
    public boolean executeTurn(Turn<?> turn) {
        if (turn instanceof ArmorCategory armor) {
            turn.player().addArmor(armor.armor());
        }
        if (turn instanceof BuffCategory buff) {
            List<Buff> buffs = buff.buffs();
            NeoBB.LOG.info("{}: Applying {} buffs from turn.", id, buffs.size());
            buffs.forEach(b -> b.apply(turn.player()));
        }
        if (turn instanceof CounterCategory counter) {
            if (counter.counterLogic(turn)) {
                NeoBB.LOG.info("{}: Misplaced/misused counter.", id);
                return false;
            } else {
                NeoBB.LOG.info("{}: Giving back health, due to counter.", id);
            }
        }
        if (turn instanceof DefensiveCategory defense) {
            NeoBB.LOG.info("{}: Applying defense from turn.", id);
            defense.applyDefense();
        }

        return true;
    }

    @Override
    public void postTurn(Turn<?> turn, boolean skipped) {
        super.postTurn(turn, skipped);
    }
}
