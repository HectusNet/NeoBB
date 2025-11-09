package net.hectus.neobb

import net.hectus.neobb.game.mode.CardGame
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.modes.turn.card_game.*
import net.hectus.neobb.modes.turn.default_game.*
import net.hectus.neobb.modes.turn.person_game.*
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers

object Registry {
    val turns = listOf(
        TTimeLimit, TDefaultWarp,

        // CardTurns.kt
        CTChest,            CTDaylightDetector, CTFlowerPot,        CTJackOLantern,
        CTOakTrapdoor,      CTPointedDripstone, CTRedstoneLamp,     CTTorch,
        CTWaxedExposedCutCopperStairs,

        // InteractableCardTurns.kt
        CTOakDoor,          CTOakFenceGate,

        // BlockTurns.kt
        TBeeNest,           TBlackWool,         TBlueBed,           TBlueIce,
        TBrainCoralBlock,   TCampfire,          TCauldron,          TComposter,
        TCyanCarpet,        TDragonHead,        TDriedKelpBlock,    TFenceGate,
        TFire,              TFireCoral,         TFireCoralFan,      TGoldBlock,
        TGreenBed,          TGreenCarpet,       TGreenWool,         THayBlock,
        THoneyBlock,        THornCoral,         TIronTrapdoor,      TLava,
        TLever,             TLightBlueWool,     TLightningRod,      TMagentaGlazedTerracotta,
        TMagmaBlock,        TMangroveRoots,     TNetherrack,        TOakStairs,
        TOrangeWool,        TPackedIce,         TPinkBed,           TPiston,
        TPowderSnow,        TPurpleWool,        TRedBed,            TRedCarpet,
        TRepeater,          TRespawnAnchor,     TSculk,             TSeaLantern,
        TSoulSand,          TSponge,            TSpruceLeaves,      TSpruceTrapdoor,
        TStonecutter,       TVerdantFroglight,  TWater,             TWhiteWool,

        // FlowerTurns.kt
        TAllium,            TAzureBluet,        TBlueOrchid,        TCornflower,
        TDirt,              TFlowerPot,         TOrangeTulip,       TOxeyeDaisy,
        TPinkTulip,         TPoppy,             TRedTulip,          TSunflower,
        TWhiteTulip,        TWitherRose,

        // ItemTurns.kt
        TChorusFruit,       TIronShovel,

        // MobTurns.kt
        TAxolotl,           TBee,               TBlaze,             TEvoker,
        TPhantom,           TPiglin,            TPolarBear,         TPufferfish,
        TSheep,

        // OtherTurns.kt
        TBoat,              TNoteBlock,

        // StructureTurns.kt
        TDaylightSensorLine,                    TIronBarJail,       TOakDoorTurtling,
        TPumpkinWall,       TRedstoneWall,      TBlueGlassWall,     TGlassWall,
        TGreenGlassWall,    TOrangeGlassWall,   TPinkGlassWall,     TRedGlassWall,
        TWhiteGlassWall,

        // ThrowableTurns.kt
        TEnderPearl,        TSnowball,          TSplashJumpBoostPotion,
        TSplashLevitationPotion,                TSplashWaterBottle,

        // WarpTurns.kt
        TAmethystWarp,      TCliffWarp,         TDesertWarp,        TEndWarp,
        TFrozenWarp,        TMeadowWarp,        TMushroomWarp,      TNerdWarp,
        TNetherWarp,        TOceanWarp,         TRedstoneWarp,      TSunWarp,
        TVoidWarp,          TWoodWarp,

        // BlockTurns.kt
        PTAmethystBlock,    PTBambooButton,     PTBarrel,           PTBeeNest,
        PTBirchLog,         PTBlackCarpet,      PTBlueConcrete,     PTBlueIce,
        PTBlueStainedGlass, PTBrainCoral,       PTBrownStainedGlass,
        PTCake,             PTCherryButton,     PTCherryPressurePlate,
        PTDaylightDetector, PTDiamondBlock,     PTDripstone,        PTFenceGate,
        PTFletchingTable,   PTGlowstone,        PTGoldBlock,        PTGrayWool,
        PTGreenCarpet,      PTHoneyBlock,       PTIronTrapdoor,     PTLever,
        PTLightBlueCarpet,  PTNoteBlock,        PTOrangeWool,       PTPinkCarpet,
        PTPurpleWool,       PTRedStainedGlass,  PTRedWool,          PTSeaLantern,
        PTStonecutter,      PTVerdantFroglight, PTWhiteStainedGlass,
        PTWhiteWool,

        // ItemTurns.kt
        PTSuspiciousStew,

        // OtherTurns.kt
        PTArmorStand,       PTPainting,

        // StructureTurns.kt
        PTCandleCircle,     PTPumpkinWall,      PTStoneWall,        PTTorchCircle,
        PTTurtling,         PTWoodWall,

        // ThrowableTurns.kt
        PTSnowball,         PTSplashPotion,

        // WarpTurns.kt
        PTAmethystWarp,     PTFireWarp,         PTIceWarp,          PTSnowWarp,
        PTVillagerWarp,     PTVoidWarp,
    ).sortedBy { it.namespace }

    val modes = listOf(
        CardGame,
        DefaultGame,
        PersonGame,
//        PvpGame,
    ).associateBy { it.gameInfo.namespace }

    val colors = listOf(
        Colors.ACCENT, Colors.SECONDARY,
        Colors.GREEN, Colors.YELLOW, Colors.RED, Colors.BLUE,
        Colors.PERSON_0, Colors.PERSON_1, Colors.PERSON_2, Colors.PERSON_3, Colors.PERSON_4,
        Colors.POSITIVE, Colors.NEUTRAL, Colors.NEGATIVE, Colors.LINK,
        Colors.EXTRA, Colors.RESET,
    )

    val modifiers = Modifiers.Player.entries + Modifiers.Player.Default.entries + Modifiers.Game.entries + Modifiers.Game.Person.entries

    /**
     * Doesn't do any loading logic, but still forces this singleton object to initialize itself.
     *
     * Also shows some information about the loaded registry elements.
     */
    fun load() {
        NeoBB.LOG.info("[Registry] Loaded ${turns.size} turns as list.")
        NeoBB.LOG.info("[Registry] Loaded ${turns.count { it is WarpTurn }} warps as part of turn list.")

        NeoBB.LOG.info("[Registry] Loaded ${modes.size} modes as map.")
        NeoBB.LOG.info("[Registry] Loaded ${colors.size} colors as list.")
        NeoBB.LOG.info("[Registry] Loaded ${modifiers.size} modifiers as list.")

        NeoBB.LOG.info("[Registry] Loaded ${StructureManager.LOADED.size} structures externally.")
    }
}
