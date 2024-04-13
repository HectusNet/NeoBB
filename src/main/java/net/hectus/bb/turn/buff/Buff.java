package net.hectus.bb.turn.buff;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.player.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class Buff {
    public enum Target { YOU, BOTH, OPPONENT }
    public enum Result {
        GOOD, MID, BAD;

        public NamedTextColor color() {
            return this == GOOD ? NamedTextColor.GREEN : (this == MID ? NamedTextColor.YELLOW : NamedTextColor.RED);
        }
    }

    protected final Target target;

    protected Buff(Target target) {
        this.target = target;
    }

    public abstract void apply(PlayerData player);
    public abstract String text(Locale l);
    public abstract Result result();

    public Component line(Locale l) {
        return Component.text("   | " + text(l) + (target != Target.YOU ? "" : " " + Translation.string(l, "item-lore.buff." + target.name().toLowerCase())), result().color());
    }

    public static class ExtraTurn extends Buff {
        private final int turns;

        public ExtraTurn(Target target, int turns) {
            super(target);
            this.turns = turns;
        }

        public ExtraTurn(Target target) {
            super(target);
            this.turns = 1;
        }

        @Override
        public void apply(@NotNull PlayerData player) {
            player.addExtraTurns(turns);
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.extra-turn" + (turns > 1 ? "s" : ""), turns);
        }

        @Override
        public Result result() {
            return switch (target) {
                case YOU -> Result.GOOD;
                case BOTH -> Result.MID;
                case OPPONENT -> Result.BAD;
            };
        }
    }

    public static class Luck extends Buff {
        private final int luck;

        public Luck(Target target, int luck) {
            super(target);
            this.luck = luck;
        }

        @Override
        public void apply(@NotNull PlayerData player) {
            player.addLuck(luck);
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.luck", luck);
        }

        @Override
        public Result result() {
            return switch (target) {
                case YOU -> luck > 0 ? Result.GOOD : Result.BAD;
                case BOTH -> Result.MID;
                case OPPONENT -> luck > 0 ? Result.BAD : Result.GOOD;
            };
        }
    }

    public static class Effect extends Buff {
        private final PotionEffect effect;

        public Effect(Target target, PotionEffect effect) {
            super(target);
            this.effect = effect;
        }

        public Effect(Target target, PotionEffectType type) {
            super(target);
            this.effect = new PotionEffect(type, -1, 0, true);
        }

        @Override
        public void apply(@NotNull PlayerData player) {
            player.player().addPotionEffect(effect);
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.effect." + effect.getType().getKey().getKey()) + (effect.getAmplifier() == 0 ? "" : " " + effect.getAmplifier());
        }

        @Override
        public Result result() {
            return switch (target) {
                case YOU -> switch (effect.getType().getEffectCategory()) {
                    case BENEFICIAL -> Result.GOOD;
                    case HARMFUL -> Result.BAD;
                    case NEUTRAL -> Result.MID;
                };
                case BOTH -> Result.MID;
                case OPPONENT -> switch (effect.getType().getEffectCategory()) {
                    case BENEFICIAL -> Result.BAD;
                    case HARMFUL -> Result.GOOD;
                    case NEUTRAL -> Result.MID;
                };
            };
        }
    }
}
