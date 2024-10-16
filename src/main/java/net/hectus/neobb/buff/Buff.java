package net.hectus.neobb.buff;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.player.Target;
import net.hectus.neobb.player.TargetObj;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Modifiers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class Buff {
    public enum BuffTarget { YOU, NEXT, OPPONENTS, ALL }

    protected final BuffTarget buffTarget;

    protected Buff(BuffTarget buffTarget) {
        this.buffTarget = buffTarget;
    }

    public abstract void apply(NeoPlayer source);
    public abstract String text(Locale l);
    public abstract TextColor color();

    public Component line(Locale l) {
        return Component.text("   | " + text(l) + (buffTarget == BuffTarget.YOU ? "" : " " + Translation.string(l, "item-lore.buff." + buffTarget.name().toLowerCase())), color());
    }

    public Target getTarget(NeoPlayer source) {
        return switch (buffTarget) {
            case YOU -> source;
            case NEXT -> source.nextPlayer();
            case OPPONENTS -> new TargetObj(source.opponents(true));
            case ALL -> source.game.gameTarget(true);
        };
    }

    public static class ExtraTurn extends Buff {
        protected final int turns;

        public ExtraTurn(BuffTarget buffTarget, int turns) {
            super(buffTarget);
            this.turns = turns;
        }

        public ExtraTurn() {
            this(BuffTarget.YOU, 1);
        }

        @Override
        public void apply(@NotNull NeoPlayer source) {
            Target target = getTarget(source);
            if (target instanceof NeoPlayer player) {
                player.addModifier(Modifiers.P_EXTRA_TURN);

                if (turns > 1) { // Not nice, but works. Too lazy to create a proper way.
                    for (int i = 1; i < turns; i++)
                        player.game.turnScheduler.runTaskLater("extra-turn-" + player.player.getName(), () -> player.addModifier(Modifiers.P_EXTRA_TURN), i);
                }
            } else if (target instanceof TargetObj players) {
                players.players().forEach(p -> p.addModifier(Modifiers.P_EXTRA_TURN));
            }
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.extra-turn" + (turns > 1 ? "s" : ""), turns);
        }

        @Override
        public TextColor color() {
            return switch (buffTarget) {
                case YOU -> Colors.POSITIVE;
                case ALL -> Colors.NEUTRAL;
                case NEXT, OPPONENTS -> Colors.NEGATIVE;
            };
        }
    }

    public static class Luck extends Buff {
        protected final int luck;

        public Luck(BuffTarget buffTarget, int luck) {
            super(buffTarget);
            this.luck = luck;
        }

        @Override
        public void apply(@NotNull NeoPlayer source) {
            source.addLuck(luck);
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.luck", luck);
        }

        @Override
        public TextColor color() {
            return switch (buffTarget) {
                case YOU -> luck > 0 ? Colors.POSITIVE : Colors.NEGATIVE;
                case ALL -> Colors.NEUTRAL;
                case NEXT, OPPONENTS -> luck > 0 ? Colors.NEGATIVE : Colors.POSITIVE;
            };
        }
    }

    public static class Effect extends Buff {
        public final PotionEffect effect;

        public Effect(BuffTarget buffTarget, PotionEffectType type, int amplifier) {
            super(buffTarget);
            this.effect = new PotionEffect(type, -1, amplifier, true);
        }

        public Effect(BuffTarget buffTarget, PotionEffectType type) {
            this(buffTarget, type, 0);
        }

        @Override
        public void apply(NeoPlayer source) {
            Target target = getTarget(source);
            if (target instanceof NeoPlayer player) {
                player.player.addPotionEffect(effect);
            } else if (target instanceof TargetObj targetObj) {
                targetObj.players().forEach(p -> p.player.addPotionEffect(effect));
            }
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.effect." + effect.getType().getKey().getKey()) + (effect.getAmplifier() == 0 ? "" : " " + effect.getAmplifier());
        }

        @Override
        public TextColor color() {
            return switch (buffTarget) {
                case YOU -> switch (effect.getType().getEffectCategory()) {
                    case BENEFICIAL -> Colors.POSITIVE;
                    case NEUTRAL -> Colors.NEUTRAL;
                    case HARMFUL -> Colors.NEGATIVE;
                };
                case ALL -> Colors.NEUTRAL;
                case NEXT, OPPONENTS -> switch (effect.getType().getEffectCategory()) {
                    case BENEFICIAL -> Colors.NEGATIVE;
                    case NEUTRAL -> Colors.NEUTRAL;
                    case HARMFUL -> Colors.POSITIVE;
                };
            };
        }
    }

    public static class Teleport extends Buff {
        public Teleport(BuffTarget buffTarget) {
            super(buffTarget);
        }

        @Override
        public void apply(@NotNull NeoPlayer source) {
            if (source.game.history().isEmpty()) return; // Should not happen in any case.

            Target target = getTarget(source);
            if (target instanceof NeoPlayer player) {
                player.player.teleport(source.game.history().getLast().location());
            } else if (target instanceof TargetObj targetObj) {
                targetObj.players().forEach(p -> p.player.teleport(source.game.history().getLast().location()));
            }
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.teleportation");
        }

        @Override
        public TextColor color() {
            return Colors.GREEN;
        }
    }

    public static class Give extends Buff {
        protected final Turn<?> turn;

        public Give(BuffTarget buffTarget, Turn<?> turn) {
            super(buffTarget);
            this.turn = turn;
        }

        @Override
        public void apply(NeoPlayer source) {
            Target target = getTarget(source);
            if (target instanceof NeoPlayer player) {
                turn.items().forEach(i -> player.inventory.addToDeck(i, turn));
            } else if (target instanceof TargetObj targetObj) {
                turn.items().forEach(i -> targetObj.players().forEach(p -> p.inventory.addToDeck(i, turn)));
            }
        }

        @Override
        public String text(Locale l) {
            return Translation.string(l, "item-lore.buff.give", turn.items().stream().mapToInt(ItemStack::getAmount).sum(), PlainTextComponentSerializer.plainText().serialize(turn.items().getFirst().displayName()));
        }

        @Override
        public TextColor color() {
            return switch (buffTarget) {
                case YOU -> Colors.POSITIVE;
                case ALL -> Colors.NEUTRAL;
                case NEXT, OPPONENTS -> Colors.NEGATIVE;
            };
        }
    }
}
