package org.secuso.privacyfriendlymemory.ui;


public class MemoImageAdapter extends android.widget.BaseAdapter {
    private final android.content.Context context;

    private final org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider layoutProvider;

    private java.util.Map<java.lang.Integer, android.graphics.Bitmap> positionBitmapCache = new java.util.HashMap<>();

    private final android.net.Uri notFoundUri;

    private final android.graphics.Bitmap notFoundBitmap;

    public MemoImageAdapter(android.content.Context context, org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider layoutProvider) {
        this.context = context;
        this.layoutProvider = layoutProvider;
        this.notFoundUri = android.net.Uri.parse(("android.resource://org.secuso.privacyfriendlymemory/" + (R.drawable.secuso_not_found)));
        this.notFoundBitmap = decodeUri(notFoundUri, layoutProvider.getCardSizePixel());
    }

    @java.lang.Override
    public int getCount() {
        return layoutProvider.getDeckSize();
    }

    @java.lang.Override
    public java.lang.Object getItem(int position) {
        return null;
    }

    @java.lang.Override
    public long getItemId(int position) {
        return 0;
    }

    @java.lang.Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        android.widget.ImageView card;
        if (convertView == null) {
            card = new android.widget.ImageView(context);
            int cardSize = layoutProvider.getCardSizePixel();
            card.setLayoutParams(new android.widget.AbsListView.LayoutParams(cardSize, cardSize));
            card.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        }else {
            card = ((android.widget.ImageView) (convertView));
        }
        if (layoutProvider.isCustomDeck()) {
            android.net.Uri imageUri = layoutProvider.getImageUri(position);
            android.graphics.Bitmap bitmapForUri;
            boolean isCurrentUriNotFoundUri = notFoundUri.toString().equals(imageUri.toString());
            if (isCurrentUriNotFoundUri) {
                bitmapForUri = notFoundBitmap;
            }else
                if ((positionBitmapCache.get(position)) != null) {
                    bitmapForUri = positionBitmapCache.get(position);
                }else {
                    bitmapForUri = decodeUri(imageUri, layoutProvider.getCardSizePixel());
                    positionBitmapCache.put(position, bitmapForUri);
                }
            
            card.setImageBitmap(bitmapForUri);
        }else {
            card.setImageResource(layoutProvider.getImageResID(position));
        }
        return card;
    }

    private android.graphics.Bitmap decodeUri(android.net.Uri uri, final int requiredSize) {
        android.graphics.BitmapFactory.Options o = new android.graphics.BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try {
            android.graphics.BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        int width_tmp = o.outWidth;
        int height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (((width_tmp / 2) < requiredSize) || ((height_tmp / 2) < requiredSize))
                break;
            
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        } 
        android.graphics.BitmapFactory.Options o2 = new android.graphics.BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            return android.graphics.BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o2);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

