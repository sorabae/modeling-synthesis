package org.secuso.privacyfriendlymemory.ui.navigation;

import static android.R.id.home;

public class DeckChoiceActivity extends org.secuso.privacyfriendlymemory.ui.AppCompatPreferenceActivity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.menu_settings);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @java.lang.Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @java.lang.Override
    public boolean onIsMultiPane() {
        return org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.isXLargeTablet(this);
    }

    private static boolean isXLargeTablet(android.content.Context context) {
        return ((context.getResources().getConfiguration().screenLayout) & (android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)) >= (android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE);
    }

    @java.lang.Override
    @android.annotation.TargetApi(value = android.os.Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(java.util.List<android.preference.PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.pref_deckchoice_headers, target);
    }

    protected boolean isValidFragment(java.lang.String fragmentName) {
        return (android.preference.PreferenceFragment.class.getName().equals(fragmentName)) || (org.secuso.privacyfriendlymemory.ui.navigation.DeckChoiceActivity.HelpFragment.class.getName().equals(fragmentName));
    }

    @android.annotation.TargetApi(value = android.os.Build.VERSION_CODES.HONEYCOMB)
    public static class HelpFragment extends android.preference.PreferenceFragment implements android.preference.Preference.OnPreferenceClickListener {
        private android.content.SharedPreferences sharedPreferences = null;

        private java.util.List<android.preference.CheckBoxPreference> checkBoxes = new java.util.LinkedList<>();

        private android.preference.CheckBoxPreference firstBox = null;

        private android.preference.CheckBoxPreference secondBox = null;

        private android.preference.CheckBoxPreference thirdBox = null;

        private int PICK_IMAGE_MULTIPLE = 100;

        private java.util.Set<java.lang.String> customImageUris = new java.util.HashSet<>();

        @java.lang.Override
        public void onCreate(android.os.Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_deckchoice_general);
            sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
            setupCheckboxes();
            setupSelection();
            createOptionsItemListener();
        }

        @java.lang.Override
        public boolean onPreferenceClick(android.preference.Preference preference) {
            saveSelectedCardDesign(preference.getKey());
            setupSelection();
            return true;
        }

        @java.lang.Override
        public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
            try {
                if ((requestCode == (PICK_IMAGE_MULTIPLE)) && (resultCode == (android.app.Activity.RESULT_OK))) {
                    android.content.ClipData mClipData = data.getClipData();
                    for (int i = 0; i < (mClipData.getItemCount()); i++) {
                        android.content.ClipData.Item item = mClipData.getItemAt(i);
                        android.net.Uri uri = item.getUri();
                        customImageUris.add(uri.toString());
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Log.d("DeckChoiceActivity", ("Could not pick images " + (e.getLocalizedMessage())));
            }
            int neededImageSize = (org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.Hard.getDeckSize()) / 2;
            if ((customImageUris.size()) >= neededImageSize) {
                sharedPreferences.edit().putStringSet(org.secuso.privacyfriendlymemory.Constants.CUSTOM_CARDS_URIS, customImageUris).commit();
                thirdBox.setEnabled(true);
                customImageUris.clear();
            }else {
                android.widget.Toast.makeText(getActivity(), getString(R.string.custom_deck_not_enough_images), android.widget.Toast.LENGTH_LONG).show();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private void createOptionsItemListener() {
            android.preference.Preference setCustomDeckPreference = findPreference("set_custom_deck");
            android.preference.Preference resetCustomDeckPreference = findPreference("reset_custom_deck");
            setCustomDeckPreference.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
                @java.lang.Override
                public boolean onPreferenceClick(android.preference.Preference preference) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setMessage(getResources().getString(R.string.set_custom_deck_hint));
                    builder.setPositiveButton(getResources().getString(R.string.set_custom_deck_hint_ok), new android.content.DialogInterface.OnClickListener() {
                        public void onClick(android.content.DialogInterface dialog, int id) {
                            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_OPEN_DOCUMENT);
                            intent.setType("image/*");
                            intent.putExtra(android.content.Intent.EXTRA_ALLOW_MULTIPLE, true);
                            startActivityForResult(android.content.Intent.createChooser(intent, ""), PICK_IMAGE_MULTIPLE);
                        }
                    });
                    builder.show();
                    return true;
                }
            });
            resetCustomDeckPreference.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
                @java.lang.Override
                public boolean onPreferenceClick(android.preference.Preference preference) {
                    sharedPreferences.edit().putStringSet(org.secuso.privacyfriendlymemory.Constants.CUSTOM_CARDS_URIS, new java.util.HashSet<java.lang.String>()).commit();
                    customImageUris.clear();
                    android.widget.Toast.makeText(getActivity(), getResources().getString(R.string.custom_deck_deleted), android.widget.Toast.LENGTH_SHORT).show();
                    if (thirdBox.isChecked()) {
                        saveSelectedCardDesign("deck1_key");
                    }
                    setupSelection();
                    return true;
                }
            });
        }

        private void setupCheckboxes() {
            firstBox = ((android.preference.CheckBoxPreference) (findPreference("deck1_key")));
            secondBox = ((android.preference.CheckBoxPreference) (findPreference("deck2_key")));
            thirdBox = ((android.preference.CheckBoxPreference) (findPreference("custom_deck_key")));
            checkBoxes.add(firstBox);
            checkBoxes.add(secondBox);
            checkBoxes.add(thirdBox);
            for (android.preference.CheckBoxPreference checkbox : checkBoxes) {
                checkbox.setOnPreferenceClickListener(this);
            }
        }

        private void setupSelection() {
            org.secuso.privacyfriendlymemory.model.CardDesign selectedDesign = org.secuso.privacyfriendlymemory.model.CardDesign.get(sharedPreferences.getInt(org.secuso.privacyfriendlymemory.Constants.SELECTED_CARD_DESIGN, 1));
            java.util.Set<java.lang.String> selectedCustomImages = sharedPreferences.getStringSet(org.secuso.privacyfriendlymemory.Constants.CUSTOM_CARDS_URIS, new java.util.HashSet<java.lang.String>());
            if (selectedCustomImages.isEmpty()) {
                thirdBox.setEnabled(false);
            }
            switch (selectedDesign) {
                case FIRST :
                    firstBox.setChecked(true);
                    secondBox.setChecked(false);
                    thirdBox.setChecked(false);
                    break;
                case SECOND :
                    firstBox.setChecked(false);
                    secondBox.setChecked(true);
                    thirdBox.setChecked(false);
                    break;
                case CUSTOM :
                    if (selectedCustomImages.isEmpty()) {
                        firstBox.setChecked(true);
                        secondBox.setChecked(false);
                        thirdBox.setChecked(false);
                    }else {
                        firstBox.setChecked(false);
                        secondBox.setChecked(false);
                        thirdBox.setChecked(true);
                    }
                    break;
            }
        }

        private void saveSelectedCardDesign(java.lang.String checkboxKey) {
            int cardDesignValue = 1;
            switch (checkboxKey) {
                case "deck1_key" :
                    cardDesignValue = 1;
                    break;
                case "deck2_key" :
                    cardDesignValue = 2;
                    break;
                case "custom_deck_key" :
                    cardDesignValue = 3;
                    break;
            }
            sharedPreferences.edit().putInt(org.secuso.privacyfriendlymemory.Constants.SELECTED_CARD_DESIGN, cardDesignValue).commit();
        }
    }
}

