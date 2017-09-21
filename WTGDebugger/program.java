/*
 *
 * This file is automatically created by Gator.
 *
 */

package org.secuso.privacyfriendlymemory;

import org.secuso.privacyfriendlymemory.ui.AppCompatPreferenceActivity;
import android.widget.TextView;
import android.view.KeyEvent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import java.util.Random;
import android.app.Activity;
import android.view.ViewGroup;
import org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity;
import java.util.ArrayList;
import org.secuso.privacyfriendlymemory.ui.SplashActivity;

public class SynthesizedProgram {

  public SynthesizedProgram() {
  }

  public void m_Menu() throws Exception {
    Random random = new Random();
    switch (random.nextInt(100)) {
    	case 0 : 
    		Random random_branch = new Random();
    		while (random_branch.nextInt) {
    			// implicit_rotate_event
    			Windows.w_AppCompatPreferenceActivity.onStop();
    			Windows.w_AppCompatPreferenceActivity.onDestroy();
    			Windows.w_HighscoreActivity.onCreate(null);
    			Windows.w_HighscoreActivity.onCreateOptionsMenu(w_Menu);
    Util.rotateOnce(solo);
    assertMenu();
    		}
    		break;
    	defult : 
    		Random random_body = new Random();
    		switch (random_body.nextInt(100)) {
    			case 0 : 
    				// implicit_power_event
    				Windows.w_AppCompatPreferenceActivity.onStop();
    // Press POWER button
    Util.powerAndBack(solo);
    assertActivity(org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.class);
    				break;
    			case 1 : 
    				// implicit_back_event
    solo.goBack();
    assertActivity(org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.class);
    				break;
    			case 2 : 
    				// implicit_home_event
    				Windows.w_AppCompatPreferenceActivity.onStop();
    // Press HOME button
    Util.homeAndBack(solo);
    assertActivity(org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.class);
    				break;
    			defult : 
    				// click
    				Windows.w_HighscoreActivity.onOptionsItemSelected();
    handleMenuItemByText("Reset all");
    assertActivity(org.secuso.privacyfriendlymemory.ui.navigation.HighscoreActivity.class);
    		}
    		m_HighscoreActivity();
    }
  }

  /*
   * ============================== Helpers ==============================
   */
  public View findViewByText(String text) {
    boolean shown = solo.waitForText(text);
    if (!shown) return null;
    ArrayList<View> views = solo.getCurrentViews();
    for (View view : views) {
      if (view instanceof TextView) {
        TextView textView = (TextView) view;
        String textOnView = textView.getText().toString();
        if (textOnView.matches(text)) {
          Log.v(TAG, "Find View (By Text " + textOnView + "): " + view);
          return view;
        }
      }
    }
    return null;
  }

  public View getActionBarView() {
    //solo.sleep(2000);
    ArrayList<View> alViews = solo.getCurrentViews();
    for (View curView :alViews) {
      String className = curView.getClass().getName();
      if (className.endsWith("ActionBarContainer")) {
        return curView;
      }
    }
    return null;
  }

  private ArrayList<View> getActionBarItemsWithMenuButton() {
    ViewGroup ActionBarContainer = (ViewGroup) this.getActionBarView();
    ArrayList<View> ret = new ArrayList<View>();
    ViewGroup ActionMenuView = (ViewGroup) recursiveFindActionMenuView(ActionBarContainer);
    if (ActionMenuView == null) {
      //The ActionBar is empty. Should not happen
      return null;
    }
    for (int i = 0; i < ActionMenuView.getChildCount(); i++) {
      View curView = ActionMenuView.getChildAt(i);
      ret.add(curView);
    }
    return ret;
  }

  public ArrayList<View> getActionBarItems() {
    ArrayList<View> ActionBarItems = getActionBarItemsWithMenuButton();
    if (ActionBarItems == null) {
      return null;
    }
    for (int i = 0; i < ActionBarItems.size(); i++) {
      View curView = ActionBarItems.get(i);
      String className = curView.getClass().getName();
      if (className.endsWith("OverflowMenuButton")) {
        ActionBarItems.remove(i);
        return ActionBarItems;
      }
    }
    return ActionBarItems;
  }

  public View getActionBarMenuButton() {
    ArrayList<View> ActionBarItems = getActionBarItemsWithMenuButton();
    if (ActionBarItems == null) {
      return null;
    }
    for (int i = 0; i < ActionBarItems.size(); i++) {
      View curView = ActionBarItems.get(i);
      String className = curView.getClass().getName();
      if (className.endsWith("OverflowMenuButton")) {
        return curView;
      }
    }
    return null;
  }

  public View getActionBarItem(int index) {
    ArrayList<View> ActionBarItems = getActionBarItems();
    if (ActionBarItems == null) {
      //There is no ActionBar
      return null;
    }
    if (index < ActionBarItems.size()) {
      return ActionBarItems.get(index);
    } else {
      //Out of range
      return null;
    }
  }

  private View recursiveFindActionMenuView(View entryPoint) {
    String curClassName = "";
    curClassName = entryPoint.getClass().getName();
    if (curClassName.endsWith("ActionMenuView")) {
      return entryPoint;
    }
    //entryPoint is not an ActionMenuView
    if (entryPoint instanceof ViewGroup) {
      ViewGroup vgEntry = (ViewGroup)entryPoint;
      for ( int i = 0; i<vgEntry.getChildCount(); i ++) {
        View curView = vgEntry.getChildAt(i);
        View retView = recursiveFindActionMenuView(curView);

        if (retView != null) {
          //ActionMenuView was found
          return retView;
        }
      }
      //Still not found
      return null;
    } else {
      return null;
    }
  }

  public View getActionBarMenuItem(int index) {
    View ret = null;
    ArrayList<View> MenuItems = getActionBarMenuItems();
    if (MenuItems != null && index < MenuItems.size()) {
      ret = MenuItems.get(index);
    }
    return ret;
  }

  public ArrayList<View> getActionBarMenuItems() {
    ArrayList<View> MenuItems = new ArrayList<View>();
    ArrayList<View> curViews = solo.getCurrentViews();

    for (int i = 0; i < curViews.size(); i++) {
      View itemView = curViews.get(i);
      String className = itemView.getClass().getName();
      if (className.endsWith("ListMenuItemView")) {
        MenuItems.add(itemView);
      }
    }
    return MenuItems;
  }

  // Assert menu
  @SuppressWarnings("unchecked")
  public void assertMenu() {
    solo.sleep(2000);
    Class<? extends View> cls = null;
    try {
      cls = (Class<? extends View>) Class.forName("com.android.internal.view.menu.MenuView$ItemView");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    ArrayList<? extends View> views = solo.getCurrentViews(cls);
    assertTrue("Menu not open.", !views.isEmpty());
  }

  @SuppressWarnings("unchecked")
  public void handleMenuItemByText(String title) {
    View v = findViewByText(title);
    if (null != v) {
      // Menu item in option menu (or on action bar if no menu poped)
      // assertTrue("MenuItem: Not Enabled.", v.isEnabled());
      solo.clickOnText(title);
    } else {
      boolean hasMore = solo.searchText("More");
      if (hasMore) {
        solo.clickOnMenuItem("More");
        handleMenuItemByText(title);
        return;
      }
      // Menu item on action bar
      Class<? extends View> cls = null;
      try {
        cls = (Class<? extends View>) Class.forName("com.android.internal.view.menu.MenuView$ItemView");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      ArrayList<? extends View> views = solo.getCurrentViews(cls);
      if (!views.isEmpty()) {
        solo.sendKey(KeyEvent.KEYCODE_MENU); // Hide option menu
        assertTrue("Menu Not Closed", solo.waitForDialogToClose());
      }
      View actionBarView = getActionBarView();
      assertNotNull("Action Bar Not Found", actionBarView);
      boolean onActionBar = false;
      for (View abv : getActionBarItems()) {
        for (View iv : solo.getViews(abv)) {
          if (iv instanceof TextView) {
            if (((TextView) iv).getText().toString().matches(title)) {
              onActionBar = true;
              assertTrue("MenuItem: Not Clickable.", iv.isClickable());
              solo.clickOnView(iv);
              break;
            }
          }
        }
        if (onActionBar) break;
      }
      if (!onActionBar) {
        // In action bar menu
        boolean found = false;
        View abMenuButton = getActionBarMenuButton();
        assertNotNull("Action Bar Menu Button Not Found", abMenuButton);
        solo.clickOnView(abMenuButton);
        assertTrue("Action Bar Not Open", solo.waitForDialogToOpen());
        ArrayList<View> acBarMIs = getActionBarMenuItems();
        for (View item : acBarMIs) {
          for (View iv : solo.getViews(item)) {
            if (iv instanceof TextView) {
              if (((TextView) iv).getText().toString().matches(title)) {
                found = true;
                assertTrue("MenuItem: Not Clickable.", iv.isClickable());
                solo.clickOnView(iv);
                break;
              }
            }
          }
          if (found) break;
        }
        assertTrue("MenuItem: not found.", found);
      }
    }
  }

  // Assert activity
  public void assertActivity(Class<? extends Activity> cls) {
    solo.sleep(2000);
    assertFalse("Dialog or Menu shows up.", solo.waitForDialogToOpen(2000));
    assertTrue("Activity does not match.", solo.waitForActivity(cls));
  }

  static class Necessary {
    public static final AppCompatPreferenceActivity w_AppCompatPreferenceActivity = new AppCompatPreferenceActivity();
    public static final Menu w_Menu = new Menu();
    public static final HighscoreActivity w_HighscoreActivity = new HighscoreActivity();
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
