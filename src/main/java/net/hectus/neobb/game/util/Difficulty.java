package net.hectus.neobb.game.util;

public enum Difficulty {
    PEACEFUL(false, 0.0, false, true, 2.0),
    EASY(false, 0.0, false, true, 1.0),
    NORMAL(true, 0.75, true, true, 1.0),
    HARD(true, 1.0, true, false, 1.0),
    CHAMP(true, 1.5, true, false, 0.4);

    public final boolean allowRanking;
    public final double rankingMultiplier;
    public final boolean usageRules;
    public final boolean suggestions;
    public final double timeMultiplier;

    Difficulty(boolean allowRanking, double rankingMultiplier, boolean usageRules, boolean suggestions, double timeMultiplier) {
        this.allowRanking = allowRanking;
        this.rankingMultiplier = rankingMultiplier;
        this.usageRules = usageRules;
        this.suggestions = suggestions;
        this.timeMultiplier = timeMultiplier;
    }
}
