/*
 *
 * This file is automatically created by Gator.
 *
 */

package org.secuso.privacyfriendlymemory;

import org.secuso.privacyfriendlymemory.ui.MainActivity;
import org.secuso.privacyfriendlymemory.ui.MemoActivity;
import android.content.res.Configuration;
import android.view.View;
import java.util.Random;
import android.app.Activity;
import java.util.ArrayList;
import org.secuso.privacyfriendlymemory.ui.SplashActivity;

public class SynthesizedProgram {

  public SynthesizedProgram() {
  }

  public void m_MemoActivity() throws Exception {
    Random random = new Random();
    switch (random.nextInt(100)) {
    	case 0 : 
    		// item_click
    		Windows.w_MemoActivity.onItemClick(null, null, new Random().nextInt(100), null);
    		Windows.w_MemoActivity.$instance_$SinglePlayerWinDialog.onCreate(null);
    assertDialog();
    		m_MemoActivity$SinglePlayerWinDialog();
    		break;
    	case 1 : 
    		Random random_branch = new Random();
    		while (random_branch.nextInt) {
    			Random random_body = new Random();
    			switch (random_body.nextInt(100)) {
    				case 0 : 
    					// item_click
    					Windows.w_MemoActivity.onItemClick(null, null, new Random().nextInt(100), null);
    assertActivity(org.secuso.privacyfriendlymemory.ui.MemoActivity.class);
    					break;
    				case 1 : 
    					// implicit_home_event
    					Windows.w_MemoActivity.onPause();
    					Windows.w_MemoActivity.onResume();
    // Press HOME button
    Util.homeAndBack(solo);
    assertActivity(org.secuso.privacyfriendlymemory.ui.MemoActivity.class);
    					break;
    				case 2 : 
    					// implicit_power_event
    					Windows.w_MemoActivity.onPause();
    					Windows.w_MemoActivity.onResume();
    // Press POWER button
    Util.powerAndBack(solo);
    assertActivity(org.secuso.privacyfriendlymemory.ui.MemoActivity.class);
    					break;
    				defult : 
    					// implicit_rotate_event
    					Windows.w_MemoActivity.onPause();
    					Windows.w_MemoActivity.onCreate(null);
    					Windows.w_MemoActivity.onResume();
    Util.rotateOnce(solo);
    assertActivity(org.secuso.privacyfriendlymemory.ui.MemoActivity.class);
    			}
    		}
    		break;
    	case 2 : 
    		// implicit_back_event
    		Windows.w_MemoActivity.onPause();
    solo.goBack();
    assertActivity(org.secuso.privacyfriendlymemory.ui.MainActivity.class);
    		m_MainActivity();
    		break;
    	defult : 
    		// item_click
    		Windows.w_MemoActivity.onItemClick(null, null, new Random().nextInt(100), null);
    		Windows.w_MemoActivity.$instance_$DuoPlayerWinDialog.onCreate(null);
    assertDialog();
    		m_MemoActivity$DuoPlayerWinDialog();
    }
  }

  /*
   * ============================== Helpers ==============================
   */
  // Assert dialog
  @SuppressWarnings("unchecked")
  public void assertDialog() {
    solo.sleep(2000);
    assertTrue("Dialog not open", solo.waitForDialogToOpen());
    Class<? extends View> cls = null;
    try {
      cls = (Class<? extends View>) Class.forName("com.android.internal.view.menu.MenuView$ItemView");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    ArrayList<? extends View> views = solo.getCurrentViews(cls);
    assertTrue("Menu not open.", views.isEmpty());
  }

  // Assert activity
  public void assertActivity(Class<? extends Activity> cls) {
    solo.sleep(2000);
    assertFalse("Dialog or Menu shows up.", solo.waitForDialogToOpen(2000));
    assertTrue("Activity does not match.", solo.waitForActivity(cls));
  }

  static class Necessary {
    public static final MainActivity w_MainActivity = new MainActivity();
    public static final MemoActivity w_MemoActivity = new MemoActivity();
  }

  // PRESTO Android Infrastructure 
  static class CommandExecutor {
    public static final String EXE_TAG = "Xewr6chA";
    public static final String REPLAY = "REPLAY";

    static void execute(Solo solo, String cmd, int delay) {
      Log.i(EXE_TAG, cmd);
      solo.sleep(delay);
    }

    static void execute(String cmd) {
      Log.i(EXE_TAG, cmd);
    }
  }

  static class Util {
    public static final String HOME_EVENT = "/data/presto/home_event";
    public static final int HOME_DELAY = 4000;
    public static final String POWER_EVENT = "/data/presto/power_event";
    public static final int POWER_DELAY = 4000;
    public static final String ROTATE1_EVENT = "/data/presto/rotate1_event";
    public static final String ROTATE2_EVENT = "/data/presto/rotate2_event";
    public static final int ROTATE_DELAY = 4000;
    public static int rotateDelay = 1000;
    public static int homeDelay = 2000;
    public static int powerDelay = 1000;
    // At first activity or any activity with similar nature, press
    // BACK to leave the app, long click the HOME button, and re-enter
    // the app.
    public static final String LEAVE_EVENT = "/data/presto/leave_event";
    public static final int LEAVE_DELAY = 4000;
    public static final String SWITCH_APP_EVENT = "/data/presto/switch_app_event";

    public static void replay(Solo solo, String event, int delay) {
      String cmd = CommandExecutor.REPLAY + " " + event;
      CommandExecutor.execute(solo, cmd, delay);
    }

    public static void rotate(Solo solo) {
      solo.setActivityOrientation(Solo.LANDSCAPE);
      solo.sleep(rotateDelay);
      solo.setActivityOrientation(Solo.PORTRAIT);
      solo.sleep(rotateDelay);
    }

    public static void rotateOnce(Solo solo) {
      int CUR_ORIENTATION = solo.getCurrentActivity().getResources().getConfiguration().orientation;
      if (CUR_ORIENTATION == Configuration.ORIENTATION_LANDSCAPE) {
        solo.setActivityOrientation(Solo.PORTRAIT);
      } else if (CUR_ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
        solo.setActivityOrientation(Solo.LANDSCAPE);
      }
    }

    // a record-replay based rotation
    public static void rrRotate(Solo solo) {
      replay(solo, ROTATE1_EVENT, ROTATE_DELAY);
      solo.sleep(rotateDelay);
      replay(solo, ROTATE2_EVENT, ROTATE_DELAY);
      solo.sleep(rotateDelay);
    }

    public static void homeAndBack(Solo solo) {
      replay(solo, HOME_EVENT, HOME_DELAY);
    }

    public static void powerAndBack(Solo solo) {
      replay(solo, POWER_EVENT, POWER_DELAY);
    }

    public static void leaveAndBack(Solo solo) {
      replay(solo, LEAVE_EVENT, LEAVE_DELAY);
    }

    public static void ent() {
      CommandExecutor.execute("EXIT");
    }
  }

}
