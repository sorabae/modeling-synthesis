package org.secuso.privacyfriendlymemory.ui;


public class MemoActivity extends org.secuso.privacyfriendlymemory.ui.MemoAppCompatDrawerActivity {
    private static android.content.Context context;

    private android.content.SharedPreferences preferences = null;

    private org.secuso.privacyfriendlymemory.model.MemoGame memory;

    private org.secuso.privacyfriendlymemory.common.MemoGameStatistics statistics;

    private org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider layoutProvider;

    private android.widget.GridView gridview;

    private java.util.Timer timerViewUpdater;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);
        super.setupNavigationView();
        setupPreferences();
        org.secuso.privacyfriendlymemory.ui.MemoActivity.context = getApplicationContext();
        createMemory();
        createStatistics();
        createLayoutProvider();
        setupGridview();
        updateStatsView();
        setupToolBar();
    }

    @java.lang.Override
    protected void onResume() {
        super.onResume();
        memory.startTimer();
        createStatistics();
    }

    @java.lang.Override
    protected void onPause() {
        super.onPause();
        memory.stopTimer();
    }

    private void setupPreferences() {
        preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void setupToolBar() {
        android.widget.TextView modeView = ((android.widget.TextView) (findViewById(R.id.gameModeText)));
        modeView.setText(memory.getMode().getStringResID());
        java.util.List<org.secuso.privacyfriendlymemory.model.MemoGameDifficulty> validDifficulties = org.secuso.privacyfriendlymemory.model.MemoGameDifficulty.getValidDifficulties();
        int difficultyCounts = validDifficulties.size();
        android.widget.RatingBar difficultyBar = ((android.widget.RatingBar) (findViewById(R.id.gameModeStar)));
        difficultyBar.setMax(difficultyCounts);
        difficultyBar.setNumStars(difficultyCounts);
        difficultyBar.setRating(((validDifficulties.indexOf(memory.getDifficulty())) + 1));
        android.widget.TextView difficultyText = ((android.widget.TextView) (findViewById(R.id.difficultyText)));
        difficultyText.setText(getString(memory.getDifficulty().getStringResID()));
        final android.widget.TextView timerView = ((android.widget.TextView) (findViewById(R.id.timerView)));
        timerViewUpdater = new java.util.Timer();
        timerViewUpdater.scheduleAtFixedRate(new java.util.TimerTask() {
            @java.lang.Override
            public void run() {
                runOnUiThread(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        timerView.setText(timeToString(memory.getTime()));
                    }
                });
            }
        }, 0, 1000);
    }

    private void setupGridview() {
        gridview = ((android.widget.GridView) (findViewById(R.id.gridview)));
        gridview.setNumColumns(layoutProvider.getColumnCount());
        final android.view.ViewGroup.MarginLayoutParams marginLayoutParams = ((android.view.ViewGroup.MarginLayoutParams) (gridview.getLayoutParams()));
        marginLayoutParams.setMargins(layoutProvider.getMarginLeft(), layoutProvider.getMargin(), layoutProvider.getMarginRight(), 0);
        gridview.setLayoutParams(marginLayoutParams);
        final org.secuso.privacyfriendlymemory.ui.MemoImageAdapter imageAdapter = new org.secuso.privacyfriendlymemory.ui.MemoImageAdapter(this, layoutProvider);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                memory.select(position);
                if (!(memory.isCustomDesign())) {
                    java.lang.Integer[] falseSelectedCards = memory.getFalseSelectedCards();
                    if (falseSelectedCards != null) {
                        java.util.List<java.lang.String> resourceNames = org.secuso.privacyfriendlymemory.common.ResIdAdapter.getResourceName(java.util.Arrays.asList(falseSelectedCards), getApplicationContext());
                        statistics.incrementCount(resourceNames);
                        java.lang.String staticsConstants = ((memory.getCardDesign()) == (org.secuso.privacyfriendlymemory.model.CardDesign.FIRST)) ? org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_ONE : org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_TWO;
                        preferences.edit().putStringSet(staticsConstants, statistics.getStatisticsSet()).commit();
                    }
                }
                imageAdapter.notifyDataSetChanged();
                updateStatsView();
                if (memory.isFinished()) {
                    saveHighscore();
                    showWinDialog();
                    gridview.setEnabled(false);
                    timerViewUpdater.cancel();
                }
            }
        });
    }

    private void updateStatsView() {
        org.secuso.privacyfriendlymemory.model.MemoGamePlayer playerOne = memory.getPlayers().get(0);
        android.widget.TextView playerOneNameView = ((android.widget.TextView) (findViewById(R.id.player_one_name)));
        playerOneNameView.setText((((getResources().getString(R.string.player_name_prefix)) + " ") + (playerOne.getNameSuffix())));
        playerOneNameView.setVisibility(android.view.View.INVISIBLE);
        android.widget.TextView playerOneFoundsValueView = ((android.widget.TextView) (findViewById(R.id.player_one_found_value)));
        java.lang.StringBuilder playerOneFoundsValueBuilder = new java.lang.StringBuilder();
        playerOneFoundsValueBuilder.append(playerOne.getFoundCardsCount()).append("/").append(((memory.getDeckSize()) / 2));
        java.lang.String playerOneFoundsValue = playerOneFoundsValueBuilder.toString();
        playerOneFoundsValueView.setText(playerOneFoundsValue);
        android.widget.TextView playerOneTriesValueView = ((android.widget.TextView) (findViewById(R.id.player_one_tries_value)));
        playerOneTriesValueView.setText(java.lang.String.valueOf(playerOne.getTries()));
        android.widget.TextView playerTwoNameView = ((android.widget.TextView) (findViewById(R.id.player_two_name)));
        android.widget.TextView playerTwoFoundsView = ((android.widget.TextView) (findViewById(R.id.player_two_found)));
        android.widget.TextView playerTwoFoundsValueView = ((android.widget.TextView) (findViewById(R.id.player_two_found_value)));
        android.widget.TextView playerTwoTriesView = ((android.widget.TextView) (findViewById(R.id.player_two_tries)));
        android.widget.TextView playerTwoTriesValueView = ((android.widget.TextView) (findViewById(R.id.player_two_tries_value)));
        if (memory.isMultiplayer()) {
            playerOneNameView.setVisibility(android.view.View.VISIBLE);
            org.secuso.privacyfriendlymemory.model.MemoGamePlayer playerTwo = memory.getPlayers().get(1);
            playerTwoNameView.setText((((getResources().getString(R.string.player_name_prefix)) + " ") + (playerTwo.getNameSuffix())));
            java.lang.StringBuilder playerTwoFoundsValueBuilder = new java.lang.StringBuilder();
            playerTwoFoundsValueBuilder.append(playerTwo.getFoundCardsCount()).append("/").append(((memory.getDeckSize()) / 2));
            java.lang.String playerTwoFoundsValue = playerTwoFoundsValueBuilder.toString();
            playerTwoFoundsValueView.setText(playerTwoFoundsValue);
            playerTwoTriesValueView.setText(java.lang.String.valueOf(playerTwo.getTries()));
        }else {
            playerTwoNameView.setText("");
            playerTwoFoundsView.setText("");
            playerTwoFoundsValueView.setText("");
            playerTwoTriesView.setText("");
            playerTwoTriesValueView.setText("");
        }
        android.widget.TextView playerOneFoundsView = ((android.widget.TextView) (findViewById(R.id.player_one_found)));
        android.widget.TextView playerOneTriesView = ((android.widget.TextView) (findViewById(R.id.player_one_tries)));
        org.secuso.privacyfriendlymemory.model.MemoGamePlayer currentPlayer = memory.getCurrentPlayer();
        int highlightColor = android.support.v4.content.ContextCompat.getColor(this, R.color.colorPrimary);
        int normalColor = android.support.v4.content.ContextCompat.getColor(this, R.color.middlegrey);
        if (currentPlayer == playerOne) {
            setColorFor(highlightColor, playerOneNameView, playerOneFoundsView, playerOneFoundsValueView, playerOneTriesView, playerOneTriesValueView);
            setColorFor(normalColor, playerTwoNameView, playerTwoFoundsView, playerTwoFoundsValueView, playerTwoTriesView, playerTwoTriesValueView);
        }else {
            setColorFor(highlightColor, playerTwoNameView, playerTwoFoundsView, playerTwoFoundsValueView, playerTwoTriesView, playerTwoTriesValueView);
            setColorFor(normalColor, playerOneNameView, playerOneFoundsView, playerOneFoundsValueView, playerOneTriesView, playerOneTriesValueView);
        }
    }

    private void setColorFor(int color, android.widget.TextView... views) {
        for (android.widget.TextView view : views) {
            view.setTextColor(color);
        }
    }

    private void saveHighscore() {
        if (!(memory.isMultiplayer())) {
            org.secuso.privacyfriendlymemory.model.MemoGameHighscore highscore = memory.getHighscore();
            int actualCore = highscore.getScore();
            int actualTries = highscore.getTries();
            int actualTime = highscore.getTime();
            org.secuso.privacyfriendlymemory.model.MemoGameDifficulty difficulty = memory.getDifficulty();
            java.lang.String highscoreConstants = "";
            java.lang.String highscoreTriesConstants = "";
            java.lang.String highscoreTimeConstants = "";
            switch (difficulty) {
                case Easy :
                    highscoreConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY;
                    highscoreTriesConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY_TRIES;
                    highscoreTimeConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_EASY_TIME;
                    break;
                case Moderate :
                    highscoreConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE;
                    highscoreTriesConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE_TRIES;
                    highscoreTimeConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_MODERATE_TIME;
                    break;
                case Hard :
                    highscoreConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD;
                    highscoreTriesConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD_TRIES;
                    highscoreTimeConstants = org.secuso.privacyfriendlymemory.Constants.HIGHSCORE_HARD_TIME;
                    break;
            }
            int currentScore = preferences.getInt(highscoreConstants, 0);
            if (actualCore > currentScore) {
                preferences.edit().putInt(highscoreConstants, actualCore).commit();
                preferences.edit().putInt(highscoreTriesConstants, actualTries).commit();
                preferences.edit().putInt(highscoreTimeConstants, actualTime).commit();
            }
        }
    }

    private void showWinDialog() {
        final android.app.Dialog dialog;
        if (memory.isMultiplayer()) {
            dialog = new org.secuso.privacyfriendlymemory.ui.MemoActivity.DuoPlayerWinDialog(this, R.style.WinDialog, memory.getPlayers());
            dialog.getWindow().setContentView(R.layout.win_duo_screen_layout);
        }else {
            dialog = new org.secuso.privacyfriendlymemory.ui.MemoActivity.SinglePlayerWinDialog(this, R.style.WinDialog, memory.getHighscore());
            dialog.getWindow().setContentView(R.layout.win_solo_screen_layout);
        }
        dialog.getWindow().setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
        final android.app.Activity activity = this;
        ((android.widget.Button) (dialog.findViewById(R.id.win_continue_button))).setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                dialog.dismiss();
                android.content.Intent intent = new android.content.Intent(activity, org.secuso.privacyfriendlymemory.ui.MainActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                activity.finish();
            }
        });
        ((android.widget.Button) (dialog.findViewById(R.id.win_showGame_button))).setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                dialog.dismiss();
            }
        });
    }

    private void createLayoutProvider() {
        layoutProvider = new org.secuso.privacyfriendlymemory.common.MemoGameLayoutProvider(this, memory);
    }

    private void createMemory() {
        android.os.Bundle intentExtras = getIntent().getExtras();
        org.secuso.privacyfriendlymemory.model.MemoGameMode mode = ((org.secuso.privacyfriendlymemory.model.MemoGameMode) (intentExtras.get(org.secuso.privacyfriendlymemory.Constants.GAME_MODE)));
        org.secuso.privacyfriendlymemory.model.MemoGameDifficulty difficulty = ((org.secuso.privacyfriendlymemory.model.MemoGameDifficulty) (intentExtras.get(org.secuso.privacyfriendlymemory.Constants.GAME_DIFFICULTY)));
        org.secuso.privacyfriendlymemory.model.CardDesign design = ((org.secuso.privacyfriendlymemory.model.CardDesign) (intentExtras.get(org.secuso.privacyfriendlymemory.Constants.CARD_DESIGN)));
        memory = new org.secuso.privacyfriendlymemory.model.MemoGame(design, mode, difficulty);
    }

    private void createStatistics() {
        if (!(memory.isCustomDesign())) {
            android.os.Bundle intentExtras = getIntent().getExtras();
            org.secuso.privacyfriendlymemory.model.CardDesign design = ((org.secuso.privacyfriendlymemory.model.CardDesign) (intentExtras.get(org.secuso.privacyfriendlymemory.Constants.CARD_DESIGN)));
            java.lang.String statisticsConstants = "";
            switch (design) {
                case FIRST :
                    statisticsConstants = org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_ONE;
                    break;
                case SECOND :
                    statisticsConstants = org.secuso.privacyfriendlymemory.Constants.STATISTICS_DECK_TWO;
                    break;
            }
            java.util.Set<java.lang.String> statisticsSet = preferences.getStringSet(statisticsConstants, new java.util.HashSet<java.lang.String>());
            statistics = new org.secuso.privacyfriendlymemory.common.MemoGameStatistics(statisticsSet);
        }
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (((newConfig.orientation) == (android.content.res.Configuration.ORIENTATION_LANDSCAPE)) || ((newConfig.orientation) == (android.content.res.Configuration.ORIENTATION_PORTRAIT))) {
            setupGridview();
        }
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
        return (((h + ":") + m) + ":") + s;
    }

    public static android.content.Context getAppContext() {
        return org.secuso.privacyfriendlymemory.ui.MemoActivity.context;
    }

    public class SinglePlayerWinDialog extends android.app.Dialog {
        private org.secuso.privacyfriendlymemory.model.MemoGameHighscore highscore;

        public SinglePlayerWinDialog(android.content.Context context, int themeResId, org.secuso.privacyfriendlymemory.model.MemoGameHighscore highscore) {
            super(context, themeResId);
            this.highscore = highscore;
        }

        @java.lang.Override
        protected void onCreate(android.os.Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((android.widget.TextView) (findViewById(R.id.win_time))).setText(timeToString(highscore.getTime()));
            ((android.widget.TextView) (findViewById(R.id.win_tries))).setText(java.lang.String.valueOf(highscore.getTries()));
            if (highscore.isValid()) {
                ((android.widget.TextView) (findViewById(R.id.win_highscore))).setText(java.lang.String.valueOf(highscore.getScore()));
            }else {
                ((android.widget.TextView) (findViewById(R.id.win_highscore_text))).setText("");
            }
        }
    }

    public class DuoPlayerWinDialog extends android.app.Dialog {
        private final org.secuso.privacyfriendlymemory.model.MemoGamePlayer playerOne;

        private final org.secuso.privacyfriendlymemory.model.MemoGamePlayer playerTwo;

        private final int cardsCount;

        public DuoPlayerWinDialog(android.content.Context context, int themeResId, java.util.List<org.secuso.privacyfriendlymemory.model.MemoGamePlayer> players) {
            super(context, themeResId);
            if ((players.size()) > 2) {
                throw new java.lang.RuntimeException("Can not create DuoPlayerWinDialog for more than 2 players");
            }
            this.playerOne = players.get(0);
            this.playerTwo = players.get(1);
            this.cardsCount = (playerOne.getFoundCardsCount()) + (playerTwo.getFoundCardsCount());
        }

        @java.lang.Override
        protected void onCreate(android.os.Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((android.widget.TextView) (findViewById(R.id.win_player_name))).setText(computeWinnerName());
            ((android.widget.TextView) (findViewById(R.id.win_first_player_name))).setText(computePlayerName(playerOne));
            ((android.widget.TextView) (findViewById(R.id.win_first_player_cards))).setText((((playerOne.getFoundCardsCount()) + "/") + (cardsCount)));
            ((android.widget.TextView) (findViewById(R.id.win_second_player_name))).setText(computePlayerName(playerTwo));
            ((android.widget.TextView) (findViewById(R.id.win_second_player_cards))).setText((((playerTwo.getFoundCardsCount()) + "/") + (cardsCount)));
        }

        private java.lang.String computePlayerName(org.secuso.privacyfriendlymemory.model.MemoGamePlayer player) {
            return ((getResources().getString(R.string.player_name_prefix)) + " ") + (player.getNameSuffix());
        }

        private java.lang.String computeWinnerName() {
            java.lang.String winnerName;
            int cardsPlayerOne = playerOne.getFoundCardsCount();
            int cardsPlayerTwo = playerTwo.getFoundCardsCount();
            if (cardsPlayerOne == cardsPlayerTwo) {
                winnerName = getResources().getString(R.string.win_text_duo_draw);
            }else
                if (cardsPlayerOne > cardsPlayerTwo) {
                    winnerName = (computePlayerName(playerOne)) + "!";
                }else {
                    winnerName = (computePlayerName(playerTwo)) + "!";
                }
            
            return winnerName;
        }
    }
}

