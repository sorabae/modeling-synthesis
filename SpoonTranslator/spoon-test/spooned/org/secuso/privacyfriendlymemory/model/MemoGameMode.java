package org.secuso.privacyfriendlymemory.model;


public enum MemoGameMode {
ONE_PLAYER(R.string.mode_single_player,R.drawable.ic_person_black_24px,1), DUO_PLAYER(R.string.mode_duo_player,R.drawable.ic_people_black_24px,2);
    private final int resIDString;

    private final int resIDImage;

    private final int playerCount;

    private static java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameMode> validTypes = new java.util.LinkedList<>();

    static {
        org.secuso.privacyfriendlymemory.model.MemoGameMode.validTypes.add(org.secuso.privacyfriendlymemory.model.MemoGameMode.ONE_PLAYER);
        org.secuso.privacyfriendlymemory.model.MemoGameMode.validTypes.add(org.secuso.privacyfriendlymemory.model.MemoGameMode.DUO_PLAYER);
    }

    MemoGameMode(@android.support.annotation.StringRes
    int resIDString, @android.support.annotation.DrawableRes
    int resIDImage, int playerCount) {
        this.resIDString = resIDString;
        this.resIDImage = resIDImage;
        this.playerCount = playerCount;
    }

    public int getStringResID() {
        return resIDString;
    }

    public int getImageResID() {
        return resIDImage;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public static java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameMode> getValidTypes() {
        return org.secuso.privacyfriendlymemory.model.MemoGameMode.validTypes;
    }
}

