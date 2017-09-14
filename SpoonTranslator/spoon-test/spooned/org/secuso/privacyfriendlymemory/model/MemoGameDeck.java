package org.secuso.privacyfriendlymemory.model;


public class MemoGameDeck {
    private java.util.Map<java.lang.Integer, org.secuso.privacyfriendlymemory.model.MemoGameCard> deck = new java.util.HashMap<>();

    public MemoGameDeck(java.util.List<java.lang.Integer> imagesResIds) {
        int matchingId = 1;
        java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameCard> cards = new java.util.LinkedList<>();
        for (java.lang.Integer imageResId : imagesResIds) {
            for (int i = 0; i <= 1; i++) {
                org.secuso.privacyfriendlymemory.model.MemoGameCard card = new org.secuso.privacyfriendlymemory.model.MemoGameCard(matchingId, imageResId, null);
                cards.add(card);
            }
            matchingId++;
        }
        java.util.Collections.shuffle(cards);
        java.lang.Integer position = 0;
        for (org.secuso.privacyfriendlymemory.model.MemoGameCard card : cards) {
            deck.put(position, card);
            position++;
        }
    }

    public MemoGameDeck(java.util.Set<android.net.Uri> imageUris) {
        int matchingId = 1;
        java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameCard> cards = new java.util.LinkedList<>();
        for (android.net.Uri imageUri : imageUris) {
            for (int i = 0; i <= 1; i++) {
                org.secuso.privacyfriendlymemory.model.MemoGameCard card = new org.secuso.privacyfriendlymemory.model.MemoGameCard(matchingId, 0, imageUri);
                cards.add(card);
            }
            matchingId++;
        }
        java.util.Collections.shuffle(cards);
        java.lang.Integer position = 0;
        for (org.secuso.privacyfriendlymemory.model.MemoGameCard card : cards) {
            deck.put(position, card);
            position++;
        }
    }

    public java.util.Map<java.lang.Integer, org.secuso.privacyfriendlymemory.model.MemoGameCard> getDeck() {
        return deck;
    }
}

