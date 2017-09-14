package org.secuso.privacyfriendlymemory.model;


public class MemoGame {
    private final org.secuso.privacyfriendlymemory.model.MemoGameDifficulty memoryDifficulty;

    private final org.secuso.privacyfriendlymemory.model.MemoGameMode memoryMode;

    private final org.secuso.privacyfriendlymemory.model.CardDesign cardDesign;

    private final java.util.Map<java.lang.Integer, org.secuso.privacyfriendlymemory.model.MemoGameCard> deck;

    private final int notFoundImageResID;

    private java.lang.Integer[] falseSelectedCards = new java.lang.Integer[2];

    private boolean falseSelected = false;

    private org.secuso.privacyfriendlymemory.model.MemoGamePlayer currentPlayer;

    private org.secuso.privacyfriendlymemory.model.MemoGameCard selectedCard = null;

    private org.secuso.privacyfriendlymemory.model.MemoGameTimer timer;

    private org.secuso.privacyfriendlymemory.model.MemoGameCard[] temporarySelectedCards = new org.secuso.privacyfriendlymemory.model.MemoGameCard[2];

    private java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameCard> foundCards = new java.util.LinkedList<>();

    private java.util.List<org.secuso.privacyfriendlymemory.model.MemoGamePlayer> players = new java.util.LinkedList<>();

    private int selectedCardsCount = 0;

    public MemoGame(org.secuso.privacyfriendlymemory.model.CardDesign cardDesign, org.secuso.privacyfriendlymemory.model.MemoGameMode memoryMode, org.secuso.privacyfriendlymemory.model.MemoGameDifficulty memoryDifficulty) {
        this.memoryDifficulty = memoryDifficulty;
        this.memoryMode = memoryMode;
        this.cardDesign = cardDesign;
        if (cardDesign == (org.secuso.privacyfriendlymemory.model.CardDesign.CUSTOM)) {
            this.deck = new org.secuso.privacyfriendlymemory.model.MemoGameDeck(org.secuso.privacyfriendlymemory.model.MemoGameCustomImages.getUris(memoryDifficulty)).getDeck();
        }else {
            this.deck = new org.secuso.privacyfriendlymemory.model.MemoGameDeck(org.secuso.privacyfriendlymemory.model.MemoGameDefaultImages.getResIDs(cardDesign, memoryDifficulty, true)).getDeck();
        }
        this.notFoundImageResID = org.secuso.privacyfriendlymemory.model.MemoGameDefaultImages.getNotFoundImageResID();
        this.players = org.secuso.privacyfriendlymemory.common.MemoGamePlayerFactory.createPlayers(memoryMode);
        this.currentPlayer = players.get(0);
        this.timer = new org.secuso.privacyfriendlymemory.model.MemoGameTimer();
    }

    public void select(int position) {
        org.secuso.privacyfriendlymemory.model.MemoGameCard cardAtPosition = deck.get(position);
        if ((isFound(cardAtPosition)) || (isSelected(cardAtPosition))) {
            return ;
        }
        switch (selectedCardsCount) {
            case 0 :
                temporarySelectedCards[0] = null;
                temporarySelectedCards[1] = null;
                selectedCard = cardAtPosition;
                (selectedCardsCount)++;
                break;
            case 1 :
                currentPlayer.incrementTries();
                if ((selectedCard.getMatchingId()) == (cardAtPosition.getMatchingId())) {
                    setFound(selectedCard, cardAtPosition);
                    if (isFinished()) {
                        stopTimer();
                    }
                }else
                    if ((players.size()) > 1) {
                        currentPlayer = getNextPlayer();
                    }
                
                if ((selectedCard.getMatchingId()) != (cardAtPosition.getMatchingId())) {
                    falseSelectedCards[0] = selectedCard.getResImageID();
                    falseSelectedCards[1] = cardAtPosition.getResImageID();
                    falseSelected = true;
                }
                temporarySelectedCards[0] = selectedCard;
                temporarySelectedCards[1] = cardAtPosition;
                selectedCard = null;
                selectedCardsCount = 0;
                break;
        }
    }

    public java.lang.Integer[] getFalseSelectedCards() {
        if (falseSelected) {
            falseSelected = false;
            return falseSelectedCards;
        }
        return null;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public int getImageResID(int position) {
        org.secuso.privacyfriendlymemory.model.MemoGameCard cardAtPosition = deck.get(position);
        if (((isFound(cardAtPosition)) || (isSelected(cardAtPosition))) || (isTemporySelected(cardAtPosition))) {
            return cardAtPosition.getResImageID();
        }
        return notFoundImageResID;
    }

    public android.net.Uri getImageUri(int position) {
        org.secuso.privacyfriendlymemory.model.MemoGameCard cardAtPosition = deck.get(position);
        if (((isFound(cardAtPosition)) || (isSelected(cardAtPosition))) || (isTemporySelected(cardAtPosition))) {
            return cardAtPosition.getImageUri();
        }
        return android.net.Uri.parse(("android.resource://org.secuso.privacyfriendlymemory/" + (R.drawable.secuso_not_found)));
    }

    public boolean isFinished() {
        return (deck.size()) == (foundCards.size()) ? true : false;
    }

    private void setFound(org.secuso.privacyfriendlymemory.model.MemoGameCard firstCard, org.secuso.privacyfriendlymemory.model.MemoGameCard secondCard) {
        foundCards.add(firstCard);
        foundCards.add(secondCard);
        currentPlayer.addFoundCard(firstCard);
        currentPlayer.addFoundCard(secondCard);
    }

    private boolean isFound(org.secuso.privacyfriendlymemory.model.MemoGameCard card) {
        return foundCards.contains(card);
    }

    private boolean isSelected(org.secuso.privacyfriendlymemory.model.MemoGameCard card) {
        return (selectedCard) == card;
    }

    private boolean isTemporySelected(org.secuso.privacyfriendlymemory.model.MemoGameCard card) {
        return ((temporarySelectedCards[0]) == card) || ((temporarySelectedCards[1]) == card);
    }

    private org.secuso.privacyfriendlymemory.model.MemoGamePlayer getNextPlayer() {
        int indexCurrentPlayer = players.indexOf(currentPlayer);
        if ((indexCurrentPlayer + 1) == (players.size())) {
            return players.get(0);
        }else {
            return players.get((indexCurrentPlayer + 1));
        }
    }

    public boolean isMultiplayer() {
        switch (memoryMode) {
            case ONE_PLAYER :
                return false;
            default :
                return true;
        }
    }

    public org.secuso.privacyfriendlymemory.model.MemoGamePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public org.secuso.privacyfriendlymemory.model.MemoGameHighscore getHighscore() {
        if (isMultiplayer()) {
            throw new java.lang.UnsupportedOperationException("Highscore is disabled in multiplayer mode!");
        }
        return new org.secuso.privacyfriendlymemory.model.MemoGameHighscore(memoryDifficulty, timer.getTime(), currentPlayer.getTries(), (!(isCustomDesign())));
    }

    public java.util.List<org.secuso.privacyfriendlymemory.model.MemoGamePlayer> getPlayers() {
        return players;
    }

    public org.secuso.privacyfriendlymemory.model.MemoGameDifficulty getDifficulty() {
        return memoryDifficulty;
    }

    public org.secuso.privacyfriendlymemory.model.MemoGameMode getMode() {
        return memoryMode;
    }

    public int getTime() {
        return timer.getTime();
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public boolean isCustomDesign() {
        return cardDesign.isCustom();
    }

    public org.secuso.privacyfriendlymemory.model.CardDesign getCardDesign() {
        return cardDesign;
    }

    public java.util.Map<java.lang.Integer, org.secuso.privacyfriendlymemory.model.MemoGameCard> getDeck() {
        return deck;
    }
}

