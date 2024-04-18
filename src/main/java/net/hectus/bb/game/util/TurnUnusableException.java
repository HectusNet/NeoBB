package net.hectus.bb.game.util;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.turn.TurnData;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class TurnUnusableException extends Exception {
    @Serial
    private static final long serialVersionUID = 5780287281626097969L;

    public TurnUnusableException(@NotNull TurnData data) {
        super(Translation.string(data.player().player().locale(), "turn.unusable." + data.turn().name().toLowerCase()));
    }
}
