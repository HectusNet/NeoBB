package net.hectus.bb.game.util;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.turn.TurnData;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Locale;

public class ImproperTurnException extends Exception {
    @Serial
    private static final long serialVersionUID = -2006627444269480825L;

    public ImproperTurnException(@NotNull ImproperTurnCause cause, @NotNull TurnData data) {
        super(cause.translated(data.player().player().locale()));
    }

    public enum ImproperTurnCause {
        WRONG_COUNTER, WRONG_WARP, ATTACK_WHILE_ATTACKED, DEFENSE_WHILE_ATTACKED, OPPONENT_ALREADY_ATTACKED, OPPONENT_DEFENDED, OPPONENT_ALSO_DEFENDED;

        public String translated(Locale l) {
            return Translation.string(l, "turn.issue." + name().toLowerCase());
        }
    }
}
