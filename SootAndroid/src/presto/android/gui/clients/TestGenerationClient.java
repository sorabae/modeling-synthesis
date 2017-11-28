/*
 * ASE15Client.java - part of the GATOR project
 *
 * Copyright (c) 2014, 2015 The Ohio State University
 *
 * This file is distributed under the terms described in LICENSE in the
 * root directory.
 */
package presto.android.gui.clients;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import presto.android.Configs;
import presto.android.Debug;
import presto.android.Logger;
import presto.android.gui.GUIAnalysisClient;
import presto.android.gui.GUIAnalysisOutput;
import presto.android.gui.clients.testgen.HelperDepot;
import presto.android.gui.clients.testgen.Path;
import presto.android.gui.clients.testgen.Robo;
import presto.android.gui.clients.testgen.Robo.TestCase;
import presto.android.gui.wtg.EventHandler;
import presto.android.gui.wtg.StackOperation;
import presto.android.gui.wtg.WTGAnalysisOutput;
import presto.android.gui.wtg.WTGBuilder;
import presto.android.gui.wtg.ds.WTG;
import presto.android.gui.wtg.ds.WTGEdge;
import presto.android.gui.wtg.ds.WTGNode;
import soot.SootClass;

import java.io.*;
import java.util.List;
import java.util.Set;

import presto.android.gui.graph.*;
import soot.SootClass;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Test case generation client.
 */
public class TestGenerationClient implements GUIAnalysisClient {
  // GUI analysis output. Pre-defined data structure providing GUI analysis output from
  // prior work published in CGO'14
  private GUIAnalysisOutput guiOutput;

  @Override
  public void run(GUIAnalysisOutput guiOutput) {
    // set start time if only wtg analysis is counted
    // if (!Configs.trackWholeExec) {
    //   Logger.verb(getClass().getSimpleName(), "Pre-running time " + Debug.v().getExecutionTime());
    //   Debug.v().setStartTime();
    // }
    // this.guiOutput = guiOutput;
    // // construct the WTGBuilder, used to build WTG
    // WTGBuilder wtgBuilder = new WTGBuilder();
    // wtgBuilder.build(guiOutput);
    // // build WTGAnalysisOutput, which provides access to WTG and other related functionalities
    // WTGAnalysisOutput wtgOutput = new WTGAnalysisOutput(this.guiOutput, wtgBuilder);
    
    // sample code retrieving and using the WTG
    // traverseWtgExample(wtgOutput);

    // print the numbers of paths for Table IV in the ASE'15 paper
    // doPathExploration(wtgOutput);

    // Generate test cases
    // generateTestCases(wtgOutput);

    System.out.println(guiOutput);

    // Flow of test generation
    // 1. Load selected subgraph of WTG from JSON file
    WTG wtg = new WTG();
    String fileDir = "/Users/cce13/dev/modeling-synthesis/WTGDebugger";

    try {
      String SEP = File.separator;
      FileReader reader = new FileReader(fileDir + SEP + "selected.json");

      String line;

      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObj = (JSONObject) jsonParser.parse(reader);

      JSONArray nodes = (JSONArray) jsonObj.get("nodes");

      for (int i=0; i<nodes.size(); i++) {
        JSONObject jsonNode = (JSONObject) nodes.get(i);
        System.out.println(jsonNode);
        String classname = (String) jsonNode.get("classname");
        System.out.println(classname);
        String pkg = "presto.android.gui.graph.";

        if (classname.equals(pkg + "NActivityNode")) {
          SootClass fakeClass = new SootClass(classname);
          NActivityNode newNode = new NActivityNode();
          newNode.c = fakeClass;
          System.out.println(wtg.addNode(newNode));
        }
      }

      System.out.println(wtg.getNodes());

        // 1. Add launcher node
        // 2. Add nodes: WTG.addNode(NObjectNode objNode)
        // 3. Add edges: WTG.addEdge(WTGEdge newEdge)
    } catch (Exception e) {
      e.printStackTrace();
    }
    // 2. Construct pseudo-WTG

    // 3. Traverse the pseudo-WTG and generate test
  }

  /**
   * Sample code to use wtg analysis output
   *
   * @param wtgOutput wtg analysis output
   */
  private void traverseWtgExample(WTGAnalysisOutput wtgOutput) {
    long execTime = Debug.v().getExecutionTime();
    WTG wtg = wtgOutput.getWTG();
    int nodeNum = 0;
    int edgeNum = 0;
    for (WTGNode node : wtg.getNodes()) {
      nodeNum++;
      for (WTGEdge outEdge : node.getOutEdges()) {
        edgeNum++;
        // print the size of push/pop sequence
        List<StackOperation> pushPopOperations = outEdge.getStackOps();
        @SuppressWarnings("unused")
        int lengthOfPushPop = pushPopOperations.size();
        // print the size of callback sequence
        List<EventHandler> callbackSequence = outEdge.getCallbacks();
        @SuppressWarnings("unused")
        int lengthOfCallback = callbackSequence.size();
      }
    }
    Logger.verb(this.getClass().getSimpleName(), "WithInlineEventHandler");
    Logger.verb(this.getClass().getSimpleName(),
        "\\texttt{" + Configs.benchmarkName
            + "}\t& nodes: " + nodeNum
            + "\t& edges: " + edgeNum
            + "\t& " + Configs.sDepth
            + "\t& " + execTime
    );
  }

  /**
   * Explore the number of (in)feasible paths. It generates numbers for Table IV
   *
   * @param wtgOutput WTG analysis output
   */
  private void doPathExploration(WTGAnalysisOutput wtgOutput) {
    WTG wtg = wtgOutput.getWTG();
    String output = Configs.benchmarkName;
    String label = getClass().getSimpleName();
    Logger.verb(getClass().getSimpleName(), "=========================================");
    for (int k = 1; k <= Configs.epDepth + 1; k++) {
      Logger.verb(label, "*************** m = " + k + " ****************");
      List<List<WTGEdge>> naive = wtgOutput.explorePaths(wtg.getLauncherNode(), k, false, Configs.allowLoop);
      int numAllPaths = naive.size();
      Logger.verb(label + ".Naive", "# of paths: " + numAllPaths);
      // use a helper method explorePaths in WTGAnalysisOutput to construct the set of all paths of length
      // at most k, starting from the WTG node for the Android launcher (this artificial node has a WTG edge
      // to the main activity of the application). The third parameter indicates whether the path feasibility
      // check should be used. The last parameter (false by default) specifies whether a WTG edge is allowed
      // to appear in the path more than once. Please see the source code of this method for more details on
      // how to perform path feasibility checks by modelling the window stack
      List<List<WTGEdge>> feasible = wtgOutput.explorePaths(wtg.getLauncherNode(), k, true, Configs.allowLoop);
      int numFeasiblePaths = feasible.size();
      Logger.verb(label + ".Feasible", "# of paths: " + numFeasiblePaths);

      Logger.verb(label + ".Compare", "% of imprv: " + (double) (numAllPaths - numFeasiblePaths) / numAllPaths);
      output += " & " + numAllPaths + " & "
          + String.format("%.1f", ((double) (numAllPaths - numFeasiblePaths) / numAllPaths) * 100) + "\\%";
    }
    output += " \\\\\n";
    Logger.verb(label + ".Latex", output);
    Logger.verb(label, "=========================================");
  }

  /**
   * Generate test cases.
   * Idea: BFS search the existing wtg. As soon as a node is
   * reached, it will never be considered again.
   * <p/>
   * NOTE: the shortestPaths may not be deterministic since there
   * may be multiple shortest paths
   */
  private void generateTestCases(WTGAnalysisOutput wtgOutput) {
    // set up test generation template parameters
    Velocity.init();
    VelocityContext context = new VelocityContext();
    SootClass mainActivityClass = guiOutput.getMainActivity();

    // String packageName = guiOutput.getAppPackageName();
    // String mainActivityName = guiOutput.getShortName();

    context.put("package", guiOutput.getAppPackageName());
    context.put("activity", mainActivityClass.getShortName());
    if (8 > Configs.getAndroidAPILevel()) {
      context.put("init", "\"" + guiOutput.getAppPackageName() + "\", " + mainActivityClass.getShortName() + ".class");
    } else {
      context.put("init", mainActivityClass.getShortName() + ".class");
    }
    context.put("activity_whole_path", mainActivityClass.getPackageName() + "." + mainActivityClass.getShortName());

    // start generating test cases
    lengthKFeasiblePathTestCases(wtgOutput, context, Configs.epDepth);
  }

  /**
   * Generate testcases for paths of length k from launcher node.
   */
  private Set<TestCase> lengthKFeasiblePathTestCases(WTGAnalysisOutput wtgOutput,
                                                     VelocityContext context, int k) {
    Logger.verb(getClass().getSimpleName(), "-----lengthKFeasiblePathTestCases-----");
    WTG wtg = wtgOutput.getWTG();
    List<List<WTGEdge>> smart = wtgOutput.exploreRealPaths(wtg.getLauncherNode(), k, true, Configs.allowLoop);
    Robo robo = new Robo(guiOutput.getAppPackageName());
    for (List<WTGEdge> path : smart) {
      robo.generateTestCase(new Path(path));
    }
    Logger.verb(getClass().getSimpleName(), "#TestCases: " + robo.cases.size());
    genCaseFile(context, "TestFeasiblePathLength" + Configs.epDepth, "Path", robo);
    return robo.cases;
  }

  /**
   * Generate test case file in Android JUnit format.
   */
  private void genCaseFile(VelocityContext context, String className, String methodName,
                           Robo robo) {
    context.put("classname", className);
    context.put("methodname", methodName);
    context.put("test_list", robo.casesAsStrings());
    context.put("import_list", robo.imports);
    context.put("helper_list", robo.helpers);
    context.put("global_list", robo.globals);
    context.put("helper_classes", robo.helperClasses);
    context.put("setup_list", robo.setups);
    try {
      if (Configs.debugCodes.contains(Debug.DUMP_TEST_CASE_DEBUG)) {
        String outputFile = new File(".").getCanonicalPath() + "/" + className + ".java";
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        Velocity.evaluate(context, writer, "Robo Test Case Template", HelperDepot.template);
        writer.flush();
        Logger.verb(getClass().getSimpleName(), "Test cases dump to: " + outputFile);
      } else {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        Logger.verb(getClass().getSimpleName(), "Start writing test cases >>>>>>>>>>");
        Velocity.evaluate(context, writer, "Robo Test Case Template", HelperDepot.template);
        writer.flush();
        Logger.verb(getClass().getSimpleName(), "Finish writing test cases <<<<<<<<<<");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}