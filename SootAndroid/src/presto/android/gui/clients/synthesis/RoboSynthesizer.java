package presto.android.gui.clients.synthesis;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import presto.android.Configs;
import presto.android.Logger;
import presto.android.gui.graph.NObjectNode;
import presto.android.gui.wtg.ds.WTGEdge;
import presto.android.gui.wtg.ds.WTGNode;
import presto.android.gui.wtg.flowgraph.NLauncherNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import presto.android.gui.wtg.EventHandler;

import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
/**
 * Main class.
 */
public class RoboSynthesizer {
  public static boolean debug = false;
  /**
   * Package name of current project.
   */
  public final String packName;
  /**
   * All test cases.
   */
  public final Set<TestCase> cases;
  /**
   * All imports.
   */
  public final Set<String> imports;
  /**
   * All helper functions.
   */
  public final Set<String> helpers;
  /**
   * All globals.
   */
  public final Set<String> globals;
  /**
   * Helper classes.
   */
  public final Set<String> helperClasses;
  /**
   * All setups.
   */
  public final List<String> setups;

  public RoboSynthesizer(String packName) {
    this.packName = packName;
    cases = Sets.newTreeSet(new Comparator<TestCase>() {
      @Override
      public int compare(TestCase testCase, TestCase t1) {
        return testCase.toString().compareTo(t1.toString());
      }
    });
    imports = Sets.newHashSet();
    helpers = Sets.newHashSet();
    globals = Sets.newLinkedHashSet();
    helperClasses = Sets.newHashSet();
    setups = Lists.newArrayList();
    if (Configs.benchmarkName.equals("SuperGenPass")) {
      globals.add("String DOMAIN = \"www.google.com\"");
      globals.add("String PASSWORD = \"123456\"");
      globals.add("int PWD_DIGIT_IDX = 0");
      setups.add("solo.setActivityOrientation(Solo.PORTRAIT);");
      setups.add("solo.clickOnMenuItem(\"Settings\");");
      setups.add("solo.clickOnText(\"Clear remembered domains*\");");
      setups.add("solo.goBack();");
// setups.add(Util.prepend("solo.enterText(0, DOMAIN);", "    "));
// setups.add(Util.prepend("solo.typeText(1, PASSWORD);", "    "));
    } else if (Configs.benchmarkName.equals("VuDroid")) {
      globals.add("int PDF_IDX = 5");
      globals.add("int DJVU_IDX = 4");
      globals.add("int DIR_IDX = 1");
      globals.add("String GotoPageNum = \"1\"");
      globals.add("String PDF = \"test.pdf\"");
      globals.add("String DJVU = \"superhero.djvu\"");
      setups.add("solo.setActivityOrientation(Solo.PORTRAIT);");
    } else if (Configs.benchmarkName.equals("OpenManager")) {
      globals.add("int DIR_IDX = 1");
      globals.add("int GZ_IDX = 2");
      globals.add("int ZIP_IDX = 3");
      globals.add("int PDF_IDX = 4");
      setups.add("solo.setActivityOrientation(Solo.PORTRAIT);");
      setups.add("if (!solo.waitForText(\"paper*\")) {\n" +
          "      CommandExecutor.execute(\"PUSH paper.pdf\");\n" +
          "      solo.sleep(3000);\n" +
          "      solo.clickOnImageButton(1);\n" +
          "    } else if (!solo.waitForText(\"png*\")) {\n" +
          "      CommandExecutor.execute(\"PUSH png.tar.gz\");\n" +
          "      solo.sleep(3000);\n" +
          "      solo.clickOnImageButton(1);\n" +
          "    } else if (!solo.waitForText(\"robotium*\")) {\n" +
          "      CommandExecutor.execute(\"PUSH robotium-master.zip\");\n" +
          "      solo.sleep(3000);\n" +
          "      solo.clickOnImageButton(1);\n" +
          "    } else if (!solo.waitForText(\"temp\")) {\n" +
          "      CommandExecutor.execute(\"MKDIR /sdcard/temp\");\n" +
          "      solo.sleep(3000);\n" +
          "      solo.clickOnImageButton(1);\n" +
          "    }");
    } else if (Configs.benchmarkName.equals("APV")) {
      globals.add("int PDF_IDX = 1");
      globals.add("int HOME_IDX = 2");
      globals.add("int DIR_IDX = 3");
      globals.add("int RECENT_IDX = 1");
      setups.add("solo.setActivityOrientation(Solo.PORTRAIT);");
    } else if (Configs.benchmarkName.equals("TippyTipper")) {
      setups.add("solo.setActivityOrientation(Solo.PORTRAIT);");
      setups.add("solo.clickOnButton(\"CLEAR\");");
    } else if (Configs.benchmarkName.equals("BarcodeScanner")) {
      globals.add("int text = 1");
      globals.add("int product = 2");
      globals.add("int wifi = 3");
      globals.add("int uri = 4");
      globals.add("int addressbook = 5");
      globals.add("int email = 6");
      globals.add("int isbn = 7");
      globals.add("int geo = 8");
      globals.add("int sms = 9");
      globals.add("int tel = 10");
      globals.add("int calendar = 11");
    } else if (Configs.benchmarkName.equals("K9")) {
      globals.add("String email = \"presto.test@yahoo.com\"");
      globals.add("String password = \"osupresto\"");
      globals.add("String stmp = \"stmp.mail.yahoo.com\"");
      globals.add("String pop = \"pop.mail.yahoo.com\"");
      globals.add("String imap = \"imap.mail.yahoo.com\"");
    } else if (Configs.benchmarkName.equals("VLC")) {
      imports.add("org.videolan.vlc.R");
    } else if (Configs.benchmarkName.equals("ConnectBot")) {
      globals.add("String password = \"RealPassword\"");
    } else if (Configs.benchmarkName.equals("KeePassDroid")) {
      globals.add("String password =\"123123\"");
      globals.add("String group = \"Internet\"");
      globals.add("String eName = \"gmail\"");
      globals.add("String eUserName = \"android.presto@gmail.com\"");
      globals.add("String eUrl = \"https://mail.google.com\"");
      globals.add("String ePwd = \"connectbot\"");
      globals.add("String eConfirmPwd = ePwd");
      globals.add("String eComment = \"test account for gmail\"");
      helpers.add("  public void addEntryIfNotExist() {\n" +
          "    if (!solo.searchText(eName)) {\n" +
          "      solo.clickOnButton(\"Add entry\");    \n" +
          "      solo.enterText(0, eName);\n" +
          "      solo.enterText(1, eUserName);\n" +
          "      solo.enterText(2, eUrl);\n" +
          "      solo.enterText(3, ePwd);\n" +
          "      solo.enterText(4, eConfirmPwd);\n" +
          "      solo.enterText(5, eComment);\n" +
          "      solo.clickOnButton(\"Save\");\n" +
          "      solo.assertCurrentActivity(getName(), \"GroupActivity\");\n" +
          "    }\n" +
          "  }");
    }
  }

  /*
   * Use existing Robotium code to synthesize program
   * representing event modeling
   */
  public TestCase synthesizeProgram(HashMap<WTGNode, List<WTGEdge>> group, List<SootClass> windows) {
    TestCase newTestCase = newTestCase();

    windows.forEach(window -> HelperDepot.addNecessaryObject(window));

    // import java.util.Random
    newTestCase.addImport("java.util.Random");

    Iterator<List<WTGEdge>> it = group.values().iterator();
    List<WTGEdge> first = it.next();

    if (!it.hasNext()) {
      synthesizeBranch(newTestCase, first);
    } else {
      int i = 0;
      newTestCase.append("Random random = new Random();");
      newTestCase.append("switch (random.nextInt(100)) {");
      newTestCase.append("\tcase " + i + " : ");
      synthesizeBranch(newTestCase, first);
      newTestCase.append("\t\tbreak;");
      i++;

      while (true) {
        List<WTGEdge> next = it.next();
        if (!it.hasNext()) {
          newTestCase.append("\tdefult : ");
          synthesizeBranch(newTestCase, next);
          break;
        } else {
          newTestCase.append("\tcase " + i + " : ");
          synthesizeBranch(newTestCase, next);
          newTestCase.append("\t\tbreak;");
          i++;
        }
      }
      newTestCase.append("}");
    }

    HelperDepot.writeNecessaryObjects(newTestCase);

    return newTestCase;
  }

  public void synthesizeBranch(TestCase testCase, List<WTGEdge> edges) {
    WTGEdge tmp = edges.get(0);
    if (tmp.getTargetNode() == tmp.getSourceNode()) {
      testCase.append("\t\tRandom random_branch = new Random();");
      testCase.append("\t\twhile (random_branch.nextInt) {");
      synthesizeBody(testCase, edges, "\t\t\t");
      testCase.append("\t\t}");
    } else {
      synthesizeBody(testCase, edges, "\t\t");
      testCase.append("\t\tm_" + tmp.getTargetNode().getWindow().getClassType().getShortName() + "();");
    }
  }

  public void synthesizeBody(TestCase testCase, List<WTGEdge> edges, String indent) {
    Iterator<WTGEdge> it = edges.iterator();
    WTGEdge first = it.next();
    if (!it.hasNext()) {
      synthesizeEdge(testCase, first, indent);
      genForEdge(testCase, first);
    } else {
      int i = 0;
      testCase.append(indent + "Random random_body = new Random();");
      testCase.append(indent + "switch (random_body.nextInt(100)) {");
      testCase.append(indent + "\tcase " + i + " : ");
      synthesizeEdge(testCase, first, indent + "\t\t");
      genForEdge(testCase, first);
      testCase.append(indent + "\t\t" + "break;");
      i++;

      while (true) {
        WTGEdge next = it.next();
        if (!it.hasNext()) {
          testCase.append(indent + "\tdefult : ");
          synthesizeEdge(testCase, next, indent + "\t\t");
          genForEdge(testCase, next);
          break;
        } else {
          testCase.append(indent + "\tcase " + i + " : ");
          synthesizeEdge(testCase, next, indent + "\t\t");
          genForEdge(testCase, next);
          testCase.append(indent + "\t\tbreak;");
          i++;
        }
      }
      testCase.append(indent + "}");
    }
  }

  /*
   * - Determine a event modeling format
   *   - In order to consider an activity that requires its previous context,
   *     we generate an activity once
   *   - For a life cycle callback,
   *     explicitly call the callback on the corresponding activity
   *     (TODO: what if data is passed to the activity by Intent?)
   *   - TODO: For an event handler callback
   *     dynamically generated callbacks such as onClick requires source-to-source translation before the analysis
   */
  public void synthesizeEdge(TestCase testCase, WTGEdge edge, String indent) {
    testCase.append(indent + "// " + edge.getEventType());
    Set<SootMethod> handlers = edge.getEventHandlers();
    if (handlers.isEmpty()) {
      System.out.println("* no event handler");
    } else {
      String widget = edge.getGUIWidget().idNode.getIdName();
      for (SootMethod handler : handlers) {
        String mth = null;
        for (Iterator<Unit> stmts = handler.retrieveActiveBody().getUnits().iterator(); stmts.hasNext();) {
          Stmt s = (Stmt) stmts.next();
          if (!(s instanceof AssignStmt)) continue;
          AssignStmt assign = (AssignStmt) s;
          Local lhs = (Local) assign.getLeftOp();
          Value rhs = assign.getRightOp();
          if (rhs instanceof StringConstant) {
            StringConstant constant = (StringConstant) rhs;
            if (!constant.value.contains("$wrapper")) continue;
            mth = constant.value;
          }
        }
        // System.out.println(handler.getSignature());
        // System.out.println(handler.getSuccessors());

        HelperDepot.addNecessaryObject(handler.getDeclaringClass());
        String cls = handler.getDeclaringClass().getShortName();
        String obj = "Windows.w_" + cls;

        if (mth == null)
          mth = handler.getName();
        String arg = "R.id." + widget;

        testCase.append(indent + obj + "." + mth + "(null, " + arg + ");");
      }
    }

    List<EventHandler> callbacks = edge.getCallbacks();
    if (callbacks.isEmpty()) {
      System.out.println("* no callbacks");
    } else {
      for (EventHandler callback : callbacks) {
        SootMethod method = callback.getEventHandler();

        HelperDepot.addNecessaryObject(method.getDeclaringClass());
        String cls = method.getDeclaringClass().getShortName();
        String obj = "Windows.w_" + cls;

        String mth = method.getName();
        String arg = "";
        if (mth.equals("onCreate")) {
          arg = "null";
        }
        testCase.append(indent + obj + "." + mth + "(" + arg + ");");
      }
    }
  }

  /**
   * @param p a path
   * @return the test case generated for the path
   */
  public TestCase generateTestCase(Path p) {
    Preconditions.checkNotNull(p);
    if (Configs.benchmarkName.equals("OpenManager")) {
      Handlers.openmanager_multiselected = false;
    }
    TestCase newTestCase = newTestCase(p);
    if (1 == p.getEdges().size() && p.getStartNode().getWindow() instanceof NLauncherNode) {
      newTestCase.append("// Start node is automatically triggered\n");
      return newTestCase;
    }
    if (debug) {
      Logger.verb(getClass().getSimpleName(), "===> path to: " + p.getEndNode());
      newTestCase.append("// Launcher node ===> " + p.getEndNode());
    }
    for (WTGEdge e : p.getEdges()) {
      if (!debug) {
        newTestCase.append("// " + e.getSourceNode().getWindow().getClassType()
            + " => " + e.getTargetNode().getWindow().getClassType());
      } else {
        newTestCase.append("// " + e.getSourceNode() + " => " + e.getTargetNode());
        int resId = HelperDepot.getResId(e.getGUIWidget());
        String idStr = null;
        if (-1 != resId) {
          idStr = e.getGUIWidget().idNode.getIdName();
        }
        newTestCase.append("// Event: " + e.getEventType() + ", on: "
            + e.getGUIWidget() + ", with id: R.id." + idStr
            + " (0x" + Integer.toHexString(resId) + "), with title: "
            + HelperDepot.getTitle(e.getGUIWidget()));
        newTestCase.append("// with handlers: " + e.getEventHandlers());
      }
      genForEdge(newTestCase, e);
    }
    return newTestCase;
  }

  /**
   * @param testCase a scaffolding test case
   * @param e        an edge
   * @return the test case generated for the edge
   */
  private TestCase genForEdge(TestCase testCase, WTGEdge e) {
    NObjectNode guiObj = e.getGUIWidget();
    if (debug) {
      int resId = HelperDepot.getResId(guiObj);
      String idStr = null;
      if (-1 != resId) {
        idStr = e.getGUIWidget().idNode.getIdName();
      }
      Logger.verb(getClass().getSimpleName(), "++ edge: " + e.getSourceNode() + " >>> " + e.getTargetNode());
      Logger.verb(getClass().getSimpleName(), "-- " + e.getEventType() + ", on: " + guiObj.getClassType()
          + ", with id: R.id." + idStr + " (0x" + Integer.toHexString(resId)
          + "), with title: " + HelperDepot.getTitle(guiObj));
    }
    SootClass cls = guiObj.getClassType();
    String objName = cls.getShortName();
    boolean finish = false;
    while (!finish) {
      try {
        if (debug) {
          Logger.verb(getClass().getSimpleName(), " |- " + "call: " + "handle" + objName + "()");
        }
        Method handler = Handlers.class.getMethod("handle" + objName, WTGEdge.class, TestCase.class);
        handler.invoke(Handlers.class, e, testCase);
        finish = true;
      } catch (NoSuchMethodException e1) {
        cls = cls.getSuperclass();
        objName = cls.getShortName();
        if (debug) {
          Logger.verb(getClass().getSimpleName(), " |- **no such method, call on its superclass: handle" + objName + "()");
        }
      } catch (SecurityException e1) {
        e1.printStackTrace();
        finish = true;
      } catch (IllegalAccessException e1) {
        e1.printStackTrace();
        finish = true;
      } catch (IllegalArgumentException e1) {
        e1.printStackTrace();
        finish = true;
      } catch (InvocationTargetException e1) {
        cls = cls.getSuperclass();
        objName = cls.getShortName();
        if (debug) {
          Logger.verb(getClass().getSimpleName(), " |- **no such event, call on its superclass: handle" + objName + "()");
        }
      }
    }
    return testCase;
  }

  /**
   * Create a new test case and add it to the test case collection.
   *
   * @return test case created
   */
  public TestCase newTestCase(final Path path) {
    TestCase newCase = new TestCase(path);
    cases.add(newCase);
    return newCase;
  }

  public TestCase newTestCase() {
    TestCase newCase = new TestCase();
    cases.add(newCase);
    return newCase;
  }

  /**
   * @return all test cases in strings.
   */
  public ArrayList<String> casesAsStrings() {
    ArrayList<String> rtn = Lists.newArrayList();
    for (TestCase c : cases) {
      rtn.add(Util.prepend(c.toCode(), "    "));
    }
    return rtn;
  }

  /**
   * Test case class
   */
  public class TestCase {
    // test case body
    private final List<String> body;
    // corresponding execution path
    private final Path path;

    private TestCase() {
      this.body = Lists.newArrayList();
      this.path = null;
    }

    private TestCase(final Path path) {
      this.body = Lists.newArrayList();
      this.path = path;
    }

    public boolean compareTo(TestCase another) {
      List<WTGEdge> thisPath = this.path.getEdges();
      List<WTGEdge> anotherPath = another.path.getEdges();
      if (thisPath.size() != anotherPath.size()) {
        return false;
      } else {
        for (int i = 0; i < thisPath.size(); i++) {
          if (thisPath.get(i) != anotherPath.get(i)) {
            return false;
          }
        }
        return true;
      }
    }

    /**
     * Add a helper function.
     *
     * @param helper the helper code
     */
    public void addHelper(String helper) {
      helpers.add(helper);
    }

    /**
     * Add an import.
     *
     * @param packName name of the imported package
     */
    public void addImport(String packName) {
      imports.add(packName);
    }

    /**
     * Add a helper class.
     *
     * @param hcls name of the helper class
     */
    public void addHelperClass(String hcls) {
      helperClasses.add(hcls);
    }

    /**
     * Add a global variable.
     *
     * @param global
     */
    public void addGlobal(String global) {
      globals.add(global);
    }

    public void addSetup(String setup) {
      setups.add(Util.prepend(setup, "    "));
    }

    /**
     * Append the body of the test case.
     *
     * @param p some code
     */
    public void append(String p) {
      String[] str = p.split("\n");
      for (String s : str) {
        body.add(s);
      }
    }

    public String getPackName() {
      return packName;
    }

    public Path getPath() {
      return path;
    }

    public String toCode() {
      String res = "";
      for (int i = 0; i < body.size(); ++i) {
        res += (i == 0 ? "" : "\n") + body.get(i);
      }
      return res;
    }

    public boolean equals(TestCase c) {
      return body.equals(c.body);
    }

    @Override
    public String toString() {
      return "RoboTestCase[" + path + "]";
    }
  }
}