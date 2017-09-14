package org.secuso.privacyfriendlymemory.ui;


public class SplashActivity extends android.support.v7.app.AppCompatActivity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.Intent mainIntent = new android.content.Intent(this, org.secuso.privacyfriendlymemory.ui.MainActivity.class);
        this.startActivity(mainIntent);
        this.finish();
    }
}

