package org.secuso.privacyfriendlymemory.model;


public class MemoGamePlayer {
    private final int namePrefixResID = R.string.player_name_prefix;

    private final java.lang.String nameSuffix;

    private int tries = 0;

    private java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameCard> foundCards = new java.util.LinkedList<>();

    public MemoGamePlayer(java.lang.String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public boolean addFoundCard(org.secuso.privacyfriendlymemory.model.MemoGameCard card) {
        return foundCards.add(card);
    }

    public int getFoundCardsCount() {
        if (foundCards.isEmpty()) {
            return 0;
        }
        return (foundCards.size()) / 2;
    }

    public java.lang.String getNameSuffix() {
        return nameSuffix;
    }

    public void incrementTries() {
        (tries)++;
    }

    public int getTries() {
        return tries;
    }
}

