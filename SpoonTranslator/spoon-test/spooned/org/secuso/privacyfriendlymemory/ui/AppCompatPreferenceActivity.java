package org.secuso.privacyfriendlymemory.ui;


public abstract class AppCompatPreferenceActivity extends android.preference.PreferenceActivity {
    private android.support.v7.app.AppCompatDelegate mDelegate;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @java.lang.Override
    protected void onPostCreate(android.os.Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    public android.support.v7.app.ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@android.support.annotation.Nullable
    android.support.v7.widget.Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @java.lang.Override
    public android.view.MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @java.lang.Override
    public void setContentView(@android.support.annotation.LayoutRes
    int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @java.lang.Override
    public void setContentView(android.view.View view) {
        getDelegate().setContentView(view);
    }

    @java.lang.Override
    public void setContentView(android.view.View view, android.view.ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @java.lang.Override
    public void addContentView(android.view.View view, android.view.ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @java.lang.Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @java.lang.Override
    protected void onTitleChanged(java.lang.CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @java.lang.Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @java.lang.Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private android.support.v7.app.AppCompatDelegate getDelegate() {
        if ((mDelegate) == null) {
            mDelegate = android.support.v7.app.AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}

