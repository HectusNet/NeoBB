package net.hectus.neobb.util;

import java.util.HashSet;
import java.util.Set;

public class Modifiers {
    public static final String P_EXTRA_TURN = "extra-turn";
    public static final String P_NO_MOVE = "no-move";
    public static final String P_NO_JUMP = "no-jump";
    public static final String P_REVIVE = "revive";

    public static final String P_DEFAULT_ATTACKED = "default-attacked";
    public static final String P_DEFAULT_DEFENDED = "default-defended";
    public static final String P_DEFAULT_100P_WARP = "default-warp-100%";
    public static final String P_DEFAULT_BOAT_DAMAGE = "default-boat-damage";
    public static final String P_DEFAULT_NO_ATTACK = "no-attack";


    public static final String G_DEFAULT_WARP_PREVENT_PREFIX = "warp-prevent.";
    public static final String G_DEFAULT_REDSTONE_POWER = "redstone-power.";

    public static final String G_LEGACY_PORTAL_AWAIT = "portal-await";
    public static final String G_LEGACY_NO_ATTACK = "no-attack";

    public static abstract class Modifiable {
        protected final Set<String> modifiers = new HashSet<>();

        public final void addModifier(String modifier) {
            modifiers.add(modifier);
        }

        public final void removeModifier(String modifier) {
            modifiers.remove(modifier);
        }

        public final boolean hasModifier(String modifier) {
            return modifiers.contains(modifier);
        }
    }
}
