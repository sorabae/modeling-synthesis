/*
 *
 * This file is automatically created by Gator.
 *
 */

package org.secuso.privacyfriendlymemory;

import org.secuso.privacyfriendlymemory.ui.MainActivity;
import org.secuso.privacyfriendlymemory.ui.MemoActivity;
import android.view.Menu;
import java.util.Random;
import org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity;
import org.secuso.privacyfriendlymemory.ui.navigation.StatisticsActivity;

public class SynthesizedProgram {

  public SynthesizedProgram() {
  }

  public void m_Menu_HighscoreActivity() throws Exception {
    		// click
    		Windows.w_HighscoreActivity.onOptionsItemSelected(null, R.idmenu_highscore_reset);
    		m_HighscoreActivity();
  }

  public void m_Menu_StatisticsActivity() throws Exception {
    		// click
    		Windows.w_StatisticsActivity.onOptionsItemSelected(null, R.idmenu_statistics_reset);
    		m_StatisticsActivity();
  }

  public void m_HighscoreActivity() throws Exception {
    		// click
    		Windows.w_HighscoreActivity.onCreateOptionsMenu(w_Menu_HighscoreActivity);
    		m_Menu_HighscoreActivity();
  }

  public void m_StatisticsActivity() throws Exception {
    		// click
    		Windows.w_StatisticsActivity.onCreateOptionsMenu(w_Menu_StatisticsActivity);
    		m_Menu_StatisticsActivity();
  }

  public void m_MainActivity() throws Exception {
    Random random = new Random();
    switch (random.nextInt(100)) {
    	case 0 : 
    		Random random_body = new Random();
    		switch (random_body.nextInt(100)) {
    			case 0 : 
    				// click
    				Windows.w_MainActivity.onClick(null, R.idarrow_left);
    				Windows.w_MemoActivity.onCreate(null);
    				Windows.w_MemoActivity.onResume();
    				break;
    			case 1 : 
    				// click
    				Windows.w_MainActivity.onClick(null, R.idarrow_right);
    				Windows.w_MemoActivity.onCreate(null);
    				Windows.w_MemoActivity.onResume();
    				break;
    			defult : 
    				// click
    				Windows.w_MainActivity.onClick(null, R.idplayButton);
    				Windows.w_MemoActivity.onCreate(null);
    				Windows.w_MemoActivity.onResume();
    		}
    		m_MemoActivity();
    		break;
    	defult : 
    		Random random_branch = new Random();
    		while (random_branch.nextInt) {
    			Random random_body = new Random();
    			switch (random_body.nextInt(100)) {
    				case 0 : 
    					// click
    					Windows.w_MainActivity.onClick(null, R.idplayButton);
    					break;
    				case 1 : 
    					// click
    					Windows.w_MainActivity.onClick(null, R.idarrow_right);
    					break;
    				defult : 
    					// click
    					Windows.w_MainActivity.onClick(null, R.idarrow_left);
    			}
    		}
    }
  }

  public void m_MemoActivity() throws Exception {
    Random random = new Random();
    switch (random.nextInt(100)) {
    	case 0 : 
    		// item_click
    		Windows.w_MemoActivity.onItemClick(null, null, new Random().nextInt(100), null);
    		Windows.w_MemoActivity.$instance_$SinglePlayerWinDialog.onCreate(null);
    		m_MemoActivity$SinglePlayerWinDialog();
    		break;
    	case 1 : 
    		Random random_branch = new Random();
    		while (random_branch.nextInt) {
    			// item_click
    			Windows.w_MemoActivity.onItemClick(null, null, new Random().nextInt(100), null);
    		}
    		break;
    	defult : 
    		// item_click
    		Windows.w_MemoActivity.onItemClick(null, null, new Random().nextInt(100), null);
    		Windows.w_MemoActivity.$instance_$DuoPlayerWinDialog.onCreate(null);
    		m_MemoActivity$DuoPlayerWinDialog();
    }
  }

  /*
   * ============================== Helpers ==============================
   */
  static class Necessary {
  public static final HighscoreActivity w_HighscoreActivity = new HighscoreActivity();
  public static final StatisticsActivity w_StatisticsActivity = new StatisticsActivity();
  public static final Menu w_Menu_HighscoreActivity = new Menu_HighscoreActivity();
  public static final MemoActivity$DuoPlayerWinDialog w_MemoActivity$DuoPlayerWinDialog = new MemoActivity$DuoPlayerWinDialog();
  public static final MemoActivity$2 w_MemoActivity$2 = new MemoActivity$2();
  public static final MemoActivity$SinglePlayerWinDialog w_MemoActivity$SinglePlayerWinDialog = new MemoActivity$SinglePlayerWinDialog();
  public static final MemoActivity w_MemoActivity = new MemoActivity();
  public static final Menu w_Menu_StatisticsActivity = new Menu_StatisticsActivity();
  public static final MainActivity w_MainActivity = new MainActivity();
  }
}
