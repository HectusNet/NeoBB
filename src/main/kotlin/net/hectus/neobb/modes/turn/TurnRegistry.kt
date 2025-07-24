package net.hectus.neobb.modes.turn

import net.hectus.neobb.modes.turn.card_game.*
import net.hectus.neobb.modes.turn.default_game.*
import net.hectus.neobb.modes.turn.person_game.*

object TurnRegistry {
    val turns = listOf<Turn<*>>(
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
        TAmethystWarp,      TCliffWarp,         TDesertWarp,        TFrozenWarp,
        TMeadowWarp,        TMushroomWarp,      TNerdWarp,          TNetherWarp,
        TOceanWarp,         TRedstoneWarp,      TSunWarp,           TVoidWarp,
        TWoodWarp,

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
}
