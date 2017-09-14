package org.secuso.privacyfriendlymemory.common;


public class MemoGameLayoutProvider {
    private static final int MARGIN_LEFT = 35;

    private static final int MARGIN_RIGHT = 35;

    private final android.content.Context context;

    private final org.secuso.privacyfriendlymemory.model.MemoGame memory;

    private final org.secuso.privacyfriendlymemory.model.MemoGameDifficulty memoryDifficulty;

    private static java.util.Map<org.secuso.privacyfriendlymemory.model.MemoGameDifficulty, java.lang.Integer> columnCountMapping = new java.util.HashMap<>();

    static {
        org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider.columnCountMapping.put(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Easy, 4);
        org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider.columnCountMapping.put(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Moderate, 6);
        org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider.columnCountMapping.put(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Hard, 8);
    }

    public MemoGameLayoutProvider(android.content.Context context, org.secuso.privacyfriendlymemory.model.MemoGame memory) {
        this.context = context;
        this.memory = memory;
        this.memoryDifficulty = memory.getDifficulty();
    }

    public int getColumnCount() {
        return org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider.columnCountMapping.get(memoryDifficulty);
    }

    public int getDeckSize() {
        return memoryDifficulty.getDeckSize();
    }

    public int getImageResID(int position) {
        return memory.getImageResID(position);
    }

    public android.net.Uri getImageUri(int position) {
        return memory.getImageUri(position);
    }

    public int getCardSizePixel() {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == (android.content.res.Configuration.ORIENTATION_PORTRAIT)) {
            int displayWidth = context.getResources().getDisplayMetrics().widthPixels;
            int cardSize = ((displayWidth - (getMarginLeft())) - (getMarginRight())) / (getColumnCount());
            return cardSize;
        }else {
            int displayHeight = context.getResources().getDisplayMetrics().heightPixels;
            int displayHeightWithoutStats = ((int) (displayHeight * (47.5F / 100.0F)));
            int cardSize = displayHeightWithoutStats / (getColumnCount());
            return cardSize;
        }
    }

    public int getMargin() {
        int displayHeight = context.getResources().getDisplayMetrics().heightPixels;
        int cardsHeight = (getColumnCount()) * (getCardSizePixel());
        int heightLeft = displayHeight - cardsHeight;
        return heightLeft / 2;
    }

    public int getMarginLeft() {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == (android.content.res.Configuration.ORIENTATION_PORTRAIT)) {
            return org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider.MARGIN_LEFT;
        }else {
            return calculateLandscapeSideMargin();
        }
    }

    public int getMarginRight() {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == (android.content.res.Configuration.ORIENTATION_PORTRAIT)) {
            return org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider.MARGIN_RIGHT;
        }else {
            return calculateLandscapeSideMargin();
        }
    }

    private int calculateLandscapeSideMargin() {
        int cardSpaceWidth = (getColumnCount()) * (getCardSizePixel());
        int displayWidth = context.getResources().getDisplayMetrics().widthPixels;
        int spaceLeft = displayWidth - cardSpaceWidth;
        return spaceLeft / 2;
    }

    public boolean isCustomDeck() {
        return memory.isCustomDesign();
    }
}

