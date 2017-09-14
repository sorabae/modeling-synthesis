package org.secuso.privacyfriendlymemory.ui.navigation;

import static android.R.id.home;

public class HighscoreActivity extends org.secuso.privacyfriendlymemory.ui.AppCompatPreferenceActivity {
    private org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.FragmentRefreshListener fragmentRefreshListener;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    public org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    @java.lang.Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_highscore, menu);
        return true;
    }

    @java.lang.Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case home :
                finish();
                return true;
            case R.id.menu_highscore_reset :
                android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY_TRIES, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY_TIME, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE_TRIES, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE_TIME, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD_TRIES, 0).commit();
                preferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD_TIME, 0).commit();
                getFragmentRefreshListener().onRefresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.menu_highscore);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @java.lang.Override
    public boolean onIsMultiPane() {
        return org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.isXLargeTablet(this);
    }

    private static boolean isXLargeTablet(android.content.Context context) {
        return ((context.getResources().getConfiguration().screenLayout) & (android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)) >= (android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE);
    }

    @java.lang.Override
    @android.annotation.TargetApi(value = android.os.Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(java.util.List<android.preference.PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.pref_highscore_headers, target);
    }

    protected boolean isValidFragment(java.lang.String fragmentName) {
        return (android.preference.PreferenceFragment.class.getName().equals(fragmentName)) || (org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.HelpFragment.class.getName().equals(fragmentName));
    }

    @android.annotation.TargetApi(value = android.os.Build.VERSION_CODES.HONEYCOMB)
    public static class HelpFragment extends android.preference.PreferenceFragment {
        @java.lang.Override
        public void onCreate(android.os.Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_highscore_general);
            setHasOptionsMenu(true);
            setHighscoreToUI();
            setHighscoreResetListener();
        }

        private void setScoreInPreference(java.lang.String preferenceKey, int score, int tries, int time) {
            android.preference.Preference preference = findPreference(preferenceKey);
            preference.setTitle(("Score:\t" + score));
            preference.setSummary((((((((getString(R.string.win_tries)) + "\t") + tries) + "\n") + (getString(R.string.win_time))) + "\t") + (timeToString(time))));
        }

        private void setHighscoreToUI() {
            android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            int highscoreEasy = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY, 0);
            int highscoreEasyTries = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY_TRIES, 0);
            int highscoreEasyTime = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY_TIME, 0);
            int highscoreModerate = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE, 0);
            int highscoreModerateTries = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE_TRIES, 0);
            int highscoreModerateTime = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE_TIME, 0);
            int highscoreHard = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD, 0);
            int highscoreHardTries = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD_TRIES, 0);
            int highscoreHardTime = preferences.getInt(org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD_TIME, 0);
            setScoreInPreference("highscore_easy", highscoreEasy, highscoreEasyTries, highscoreEasyTime);
            setScoreInPreference("highscore_moderate", highscoreModerate, highscoreModerateTries, highscoreModerateTime);
            setScoreInPreference("highscore_hard", highscoreHard, highscoreHardTries, highscoreHardTime);
        }

        private void setHighscoreResetListener() {
            ((org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity) (getActivity())).setFragmentRefreshListener(new org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.FragmentRefreshListener() {
                @java.lang.Override
                public void onRefresh() {
                    setHighscoreToUI();
                }
            });
        }

        private java.lang.String timeToString(int time) {
            int seconds = time % 60;
            int minutes = ((time - seconds) / 60) % 60;
            int hours = ((time - minutes) - seconds) / 3600;
            java.lang.String h;
            java.lang.String m;
            java.lang.String s;
            s = (seconds < 10) ? "0" + (java.lang.String.valueOf(seconds)) : java.lang.String.valueOf(seconds);
            m = (minutes < 10) ? "0" + (java.lang.String.valueOf(minutes)) : java.lang.String.valueOf(minutes);
            h = (hours < 10) ? "0" + (java.lang.String.valueOf(hours)) : java.lang.String.valueOf(hours);
            return ((((h + ":") + m) + ":") + s) + "\t (h:m:s)";
        }
    }

    public interface FragmentRefreshListener {
        void onRefresh();
    }
}

