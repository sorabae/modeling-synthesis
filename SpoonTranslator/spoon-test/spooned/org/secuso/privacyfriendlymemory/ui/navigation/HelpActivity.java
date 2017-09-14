package org.secuso.privacyfriendlymemory.ui.navigation;

import static android.R.id.home;

public class HelpActivity extends org.secuso.privacyfriendlymemory.ui.AppCompatPreferenceActivity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.menu_help);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @java.lang.Override
    public boolean onIsMultiPane() {
        return org.secuso.privacyfriendlymemory.ui.navigation.HelpActivity.isXLargeTablet(this);
    }

    private static boolean isXLargeTablet(android.content.Context context) {
        return ((context.getResources().getConfiguration().screenLayout) & (android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)) >= (android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE);
    }

    @java.lang.Override
    @android.annotation.TargetApi(value = android.os.Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(java.util.List<android.preference.PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.pref_help_headers, target);
    }

    protected boolean isValidFragment(java.lang.String fragmentName) {
        return (android.preference.PreferenceFragment.class.getName().equals(fragmentName)) || (org.secuso.privacyfriendlymemory.ui.navigation.HelpActivity.HelpFragment.class.getName().equals(fragmentName));
    }

    @android.annotation.TargetApi(value = android.os.Build.VERSION_CODES.HONEYCOMB)
    public static class HelpFragment extends android.preference.PreferenceFragment {
        @java.lang.Override
        public void onCreate(android.os.Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_help_general);
            setHasOptionsMenu(true);
        }

        @java.lang.Override
        public boolean onOptionsItemSelected(android.view.MenuItem item) {
            int id = item.getItemId();
            if (id == (home)) {
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}

