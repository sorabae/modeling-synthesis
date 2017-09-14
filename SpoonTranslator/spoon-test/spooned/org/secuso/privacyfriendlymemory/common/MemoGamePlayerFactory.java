package org.secuso.privacyfriendlymemory.common;


public class MemoGamePlayerFactory {
    public static java.util.List<org.secuso.privacyfriendlymemory.model.MemoGamePlayer> createPlayers(org.secuso.privacyfriendlymemory.model.MemoGameMode memoryMode) {
        java.util.List<org.secuso.privacyfriendlymemory.model.MemoGamePlayer> players = new java.util.LinkedList<>();
        int playerCount = memoryMode.getPlayerCount();
        org.secuso.privacyfriendlymemory.model.MemoGamePlayer player;
        for (int i = 1; i <= playerCount; i++) {
            player = new org.secuso.privacyfriendlymemory.model.MemoGamePlayer(java.lang.String.valueOf(i));
            players.add(player);
        }
        return players;
    }
}

