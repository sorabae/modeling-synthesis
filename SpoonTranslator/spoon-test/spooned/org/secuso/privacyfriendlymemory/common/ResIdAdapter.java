package org.secuso.privacyfriendlymemory.common;


public class ResIdAdapter {
    public static java.util.List<java.lang.String> getResourceName(java.util.List<java.lang.Integer> resIds, android.content.Context context) {
        java.util.List<java.lang.String> resIdResourceNames = new java.util.LinkedList<>();
        for (java.lang.Integer resId : resIds) {
            resIdResourceNames.add(context.getResources().getResourceEntryName(resId));
        }
        return resIdResourceNames;
    }
}

