package org.secuso.privacyfriendlymemory.model;


public class MemoGameCard {
    private final int matchingId;

    private final int resImageID;

    private final android.net.Uri imageUri;

    public MemoGameCard(int matchingId, @android.support.annotation.DrawableRes
    int resImageID, android.net.Uri imageUri) {
        this.matchingId = matchingId;
        this.resImageID = resImageID;
        this.imageUri = imageUri;
    }

    public int getMatchingId() {
        return matchingId;
    }

    public int getResImageID() {
        return resImageID;
    }

    public android.net.Uri getImageUri() {
        return imageUri;
    }
}

