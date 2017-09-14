package org.secuso.privacyfriendlymemory.model;


public class MemoGameCustomImages {
    public static java.util.Set<android.net.Uri> getUris(org.secuso.privacyfriendlymemory.model.MemoGameDifficulty memoryDifficulty) {
        android.content.Context context = org.secuso.privacyfriendlymemory.ui.MemoActivity.getAppContext();
        android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        java.util.Set<android.net.Uri> uris = new java.util.LinkedHashSet<>();
        for (java.lang.String uriString : preferences.getStringSet(org.secuso.privacyfriendlymemory.Constants.CUSTOM_CARDS_URIS, new java.util.LinkedHashSet<java.lang.String>())) {
            uris.add(android.net.Uri.parse(uriString));
        }
        int differentUris = (memoryDifficulty.getDeckSize()) / 2;
        if (differentUris > (uris.size())) {
            throw new java.lang.IllegalStateException("Requested deck contains not enough images for the specific game difficulty");
        }
        java.util.List<android.net.Uri> setAsList = new java.util.LinkedList<>(uris);
        return new java.util.LinkedHashSet<>(setAsList.subList(0, differentUris));
    }
}

