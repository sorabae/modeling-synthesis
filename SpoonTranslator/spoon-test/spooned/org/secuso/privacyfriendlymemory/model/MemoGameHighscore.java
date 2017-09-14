package org.secuso.privacyfriendlymemory.model;


public class MemoGameHighscore {
    private static java.util.Map<org.secuso.privacyfriendlymemory.model.MemoGameDifficulty, java.lang.Integer> baseScoreMapping = new java.util.HashMap<>();

    private final int baseScore;

    private final int time;

    private final int tries;

    private final boolean isValid;

    static {
        org.secuso.privacyfriendlymemory.model.MemoGameHighscore.baseScoreMapping.put(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Easy, 3000);
        org.secuso.privacyfriendlymemory.model.MemoGameHighscore.baseScoreMapping.put(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Moderate, 9000);
        org.secuso.privacyfriendlymemory.model.MemoGameHighscore.baseScoreMapping.put(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Hard, 50000);
    }

    public MemoGameHighscore(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty difficulty, int time, int tries, boolean isValid) {
        this.baseScore = org.secuso.privacyfriendlymemory.model.MemoGameHighscore.baseScoreMapping.get(difficulty);
        this.time = time;
        this.tries = tries;
        this.isValid = isValid;
    }

    public int getScore() {
        int calculatedScore = (baseScore) - ((time) * (tries));
        return calculatedScore < 0 ? 0 : calculatedScore;
    }

    public int getTries() {
        return tries;
    }

    public int getTime() {
        return time;
    }

    public boolean isValid() {
        return isValid;
    }
}

