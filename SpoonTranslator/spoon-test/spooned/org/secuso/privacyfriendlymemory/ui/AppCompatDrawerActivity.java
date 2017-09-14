package org.secuso.privacyfriendlymemory.ui;


public abstract class AppCompatDrawerActivity extends android.support.v7.app.AppCompatActivity implements android.support.design.widget.NavigationView.OnNavigationItemSelectedListener {
    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupNavigationView() {
        android.support.v7.widget.Toolbar toolbar = ((android.support.v7.widget.Toolbar) (findViewById(R.id.toolbar)));
        setSupportActionBar(toolbar);
        android.support.v4.widget.DrawerLayout drawer = ((android.support.v4.widget.DrawerLayout) (findViewById(R.id.menu_drawer)));
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        android.support.design.widget.NavigationView navigationView = ((android.support.design.widget.NavigationView) (findViewById(R.id.nav_view)));
        navigationView.setNavigationItemSelectedListener(this);
    }

    @java.lang.Override
    public boolean onNavigationItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        android.content.Intent intent = null;
        switch (id) {
            case R.id.menu_highscore :
                intent = new android.content.Intent(this, org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.class);
                intent.putExtra(org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.EXTRA_SHOW_FRAGMENT, org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.HelpFragment.class.getName());
                intent.putExtra(org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.EXTRA_NO_HEADERS, true);
                break;
            case R.id.menu_statistics :
                intent = new android.content.Intent(this, org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.class);
                break;
            case R.id.menu_settings :
                intent = new android.content.Intent(this, org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.class);
                intent.putExtra(org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.EXTRA_SHOW_FRAGMENT, org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.HelpFragment.class.getName());
                intent.putExtra(org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.EXTRA_NO_HEADERS, true);
                break;
            case R.id.menu_help :
                intent = new android.content.Intent(this, org.secuso.privacyfriendlymemory.ui.navigation.HelpActivity.class);
                intent.putExtra(org.secuso.privacyfriendlymemory.ui.navigation.HelpActivity.EXTRA_SHOW_FRAGMENT, org.secuso.privacyfriendlymemory.ui.navigation.HelpActivity.HelpFragment.class.getName());
                intent.putExtra(org.secuso.privacyfriendlymemory.ui.navigation.HelpActivity.EXTRA_NO_HEADERS, true);
                break;
            case R.id.menu_about :
                intent = new android.content.Intent(this, org.secuso.privacyfriendlymemory.ui.navigation.AboutActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        android.support.v4.widget.DrawerLayout drawer = ((android.support.v4.widget.DrawerLayout) (findViewById(R.id.menu_drawer)));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @java.lang.Override
    public void onBackPressed() {
        android.support.v4.widget.DrawerLayout drawer = ((android.support.v4.widget.DrawerLayout) (findViewById(R.id.menu_drawer)));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}

