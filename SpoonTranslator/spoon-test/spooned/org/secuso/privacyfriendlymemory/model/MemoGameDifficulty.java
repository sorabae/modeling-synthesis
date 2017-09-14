package org.secuso.privacyfriendlymemory.model;


public enum MemoGameDifficulty {
Easy(R.string.difficulty_easy,16), Moderate(R.string.difficulty_moderate,36), Hard(R.string.difficulty_hard,64);
    private final int resID;

    private final int deckSize;

    private static java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameDifficulty> validDifficulties = new java.util.LinkedList<>();

    static {
        org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.validDifficulties.add(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Easy);
        org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.validDifficulties.add(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Moderate);
        org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.validDifficulties.add(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Hard);
    }

    MemoGameDifficulty(@android.support.annotation.StringRes
    int resID, int deckSize) {
        this.resID = resID;
        this.deckSize = deckSize;
    }

    public int getStringResID() {
        return resID;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public static java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameDifficulty> getValidDifficulties() {
        return org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.validDifficulties;
    }
}

