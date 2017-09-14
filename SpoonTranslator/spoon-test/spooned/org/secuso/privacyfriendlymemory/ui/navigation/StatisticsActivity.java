package org.secuso.privacyfriendlymemory.ui.navigation;


public class StatisticsActivity extends android.support.v7.app.AppCompatActivity {
    private static org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity statisticsActivity;

    private org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private android.support.v4.view.ViewPager mViewPager;

    private android.content.SharedPreferences preferences = null;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistcs_content);
        preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        setupActionBar();
        this.statisticsActivity = this;
        mSectionsPagerAdapter = new org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = ((android.support.v4.view.ViewPager) (findViewById(R.id.container)));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        android.support.design.widget.TabLayout tabLayout = ((android.support.design.widget.TabLayout) (findViewById(R.id.tabs)));
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.menu_statistics);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.parseColor("#024265")));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @java.lang.Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @java.lang.Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.menu_statistics_reset :
                java.util.List<java.lang.Integer> resIdsDeckOne = org.secuso.privacyfriendlymemory.model.MemoGameDefaultImages.getResIDs(org.secuso.privacyfriendlymemory.model.CardDesign.FIRST, org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Hard, false);
                java.util.List<java.lang.Integer> resIdsDeckTwo = org.secuso.privacyfriendlymemory.model.MemoGameDefaultImages.getResIDs(org.secuso.privacyfriendlymemory.model.CardDesign.SECOND, org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Hard, false);
                java.util.List<java.lang.String> resourceNamesDeckOne = org.secuso.privacyfriendlymemory.common.ResIdAdapter.getResourceName(resIdsDeckOne, this);
                java.util.List<java.lang.String> resourceNamesDeckTwo = org.secuso.privacyfriendlymemory.common.ResIdAdapter.getResourceName(resIdsDeckTwo, this);
                java.util.Set<java.lang.String> statisticsDeckOne = org.secuso.privacyfriendlymemory.common.MemoGameStatistics.createInitStatistics(resourceNamesDeckOne);
                java.util.Set<java.lang.String> staticticsDeckTwo = org.secuso.privacyfriendlymemory.common.MemoGameStatistics.createInitStatistics(resourceNamesDeckTwo);
                preferences.edit().putStringSet(org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_ONE, statisticsDeckOne).commit();
                preferences.edit().putStringSet(org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_TWO, staticticsDeckTwo).commit();
                mSectionsPagerAdapter.refresh(getApplicationContext());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
        private android.support.v4.app.FragmentManager fm;

        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @java.lang.Override
        public android.support.v4.app.Fragment getItem(int position) {
            return org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment.newInstance(position);
        }

        @java.lang.Override
        public int getCount() {
            return 2;
        }

        @java.lang.Override
        public java.lang.CharSequence getPageTitle(int position) {
            return getString(org.secuso.privacyfriendlymemory.model.CardDesign.get((position + 1)).getDisplayNameResId());
        }

        public void refresh(android.content.Context context) {
            for (android.support.v4.app.Fragment f : fm.getFragments()) {
                if (f instanceof org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment) {
                    ((org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment) (f)).refresh(context);
                }
            }
        }
    }

    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
        private android.widget.ListView statisticsListView;

        private org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.StatisticsAdapter statisticsAdapter;

        private static final java.lang.String ARG_SECTION_NUMBER = "section_number";

        private android.view.View rootView;

        public static org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment newInstance(int sectionNumber) {
            org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment fragment = new org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment();
            android.os.Bundle args = new android.os.Bundle();
            args.putInt(org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment.ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void refresh(android.content.Context context) {
            createListViewWithAdapter();
        }

        public PlaceholderFragment() {
        }

        @java.lang.Override
        public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
            android.view.View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
            this.rootView = rootView;
            statisticsListView = ((android.widget.ListView) (rootView.findViewById(R.id.statistics_listview)));
            createListViewWithAdapter();
            return rootView;
        }

        private void createListViewWithAdapter() {
            int currentPosition = getArguments().getInt(org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.PlaceholderFragment.ARG_SECTION_NUMBER);
            org.secuso.privacyfriendlymemory.model.CardDesign cardDesignInFragmenet = org.secuso.privacyfriendlymemory.model.CardDesign.get((currentPosition + 1));
            org.secuso.privacyfriendlymemory.common.MemoGameStatistics memoryStats = createStatistics(cardDesignInFragmenet);
            statisticsAdapter = new org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.StatisticsAdapter(org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.statisticsActivity, memoryStats.getResourceNames(), memoryStats.getFalseSelectedCounts());
            statisticsListView.setAdapter(statisticsAdapter);
        }

        private org.secuso.privacyfriendlymemory.common.MemoGameStatistics createStatistics(org.secuso.privacyfriendlymemory.model.CardDesign design) {
            java.lang.String statisticsConstants = "";
            switch (design) {
                case FIRST :
                    statisticsConstants = org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_ONE;
                    break;
                case SECOND :
                    statisticsConstants = org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_TWO;
                    break;
            }
            java.util.Set<java.lang.String> statisticsSet = android.preference.PreferenceManager.getDefaultSharedPreferences(org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.statisticsActivity).getStringSet(statisticsConstants, new java.util.HashSet<java.lang.String>());
            return new org.secuso.privacyfriendlymemory.common.MemoGameStatistics(statisticsSet);
        }
    }

    static class StatisticsAdapter extends android.widget.ArrayAdapter<java.lang.String> {
        private android.app.Activity activity;

        private java.util.Map<java.lang.String, java.lang.Integer> orderedNameCountMapping = new java.util.LinkedHashMap<>();

        public StatisticsAdapter(android.app.Activity activity, java.lang.String[] resourceNames, java.lang.Integer[] falseSelectedCounts) {
            super(activity, R.layout.activity_single_statistics_entry, resourceNames);
            this.activity = activity;
            initOrderedNameCountMapping(resourceNames, falseSelectedCounts);
        }

        private void initOrderedNameCountMapping(java.lang.String[] resourceNames, java.lang.Integer[] falseSelectedCounts) {
            for (int i = 0; i < (resourceNames.length); i++) {
                orderedNameCountMapping.put(resourceNames[i], falseSelectedCounts[i]);
            }
            orderedNameCountMapping = org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity.StatisticsAdapter.sortByValue(orderedNameCountMapping);
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View view, android.view.ViewGroup parent) {
            int invertedIndex = ((orderedNameCountMapping.size()) - position) - 1;
            android.view.LayoutInflater inflater = activity.getLayoutInflater();
            android.view.View rowView = inflater.inflate(R.layout.activity_single_statistics_entry, null, true);
            android.widget.TextView txtTitle = ((android.widget.TextView) (rowView.findViewById(R.id.card_false_selected_stats)));
            txtTitle.setText(((("\t" + (activity.getResources().getString(R.string.false_selected))) + "\t") + (getCountAtPosition(invertedIndex))));
            android.widget.ImageView imageView = ((android.widget.ImageView) (rowView.findViewById(R.id.card_image)));
            int drawableResourceId = activity.getResources().getIdentifier(getResourceNameAtPosition(invertedIndex), "drawable", activity.getPackageName());
            imageView.setImageResource(drawableResourceId);
            return rowView;
        }

        private int getCountAtPosition(int position) {
            return new java.util.ArrayList<java.lang.Integer>(orderedNameCountMapping.values()).get(position);
        }

        private java.lang.String getResourceNameAtPosition(int position) {
            return new java.util.ArrayList<java.lang.String>(orderedNameCountMapping.keySet()).get(position);
        }

        public static <K, V extends java.lang.Comparable<? super V>> java.util.Map<K, V> sortByValue(java.util.Map<K, V> map) {
            java.util.List<java.util.Map.Entry<K, V>> list = new java.util.LinkedList<>(map.entrySet());
            java.util.Collections.sort(list, new java.util.Comparator<java.util.Map.Entry<K, V>>() {
                @java.lang.Override
                public int compare(java.util.Map.Entry<K, V> o1, java.util.Map.Entry<K, V> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            java.util.Map<K, V> result = new java.util.LinkedHashMap<>();
            for (java.util.Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }
    }
}

