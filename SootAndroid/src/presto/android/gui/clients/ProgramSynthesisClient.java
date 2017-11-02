package presto.android.gui.clients;

import com.google.common.collect.Sets;
import presto.android.Configs;
import presto.android.Debug;
import presto.android.Logger;
import presto.android.gui.GUIAnalysisClient;
import presto.android.gui.GUIAnalysisOutput;
import presto.android.gui.clients.energy.EnergyAnalyzer;
import presto.android.gui.clients.energy.EnergyUtils;
import presto.android.gui.clients.energy.Pair;
import presto.android.gui.clients.energy.VarUtil;
import presto.android.gui.graph.*;
import presto.android.gui.listener.EventType;
import presto.android.gui.wtg.EventHandler;
import presto.android.gui.wtg.StackOperation;
import presto.android.gui.wtg.WTGAnalysisOutput;
import presto.android.gui.wtg.WTGBuilder;
import presto.android.gui.wtg.ds.WTG;
import presto.android.gui.wtg.ds.WTGEdge;
import presto.android.gui.wtg.ds.WTGNode;
import presto.android.gui.wtg.flowgraph.NLauncherNode;
import soot.SootMethod;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
// import presto.android.gui.clients.testgen.HelperDepot;
// import presto.android.gui.clients.testgen.Path;
// import presto.android.gui.clients.testgen.Robo;
import presto.android.gui.clients.synthesis.HelperDepot;
import presto.android.gui.clients.synthesis.Path;
import presto.android.gui.clients.synthesis.RoboSynthesizer;
import soot.SootClass;

public class ProgramSynthesisClient implements GUIAnalysisClient {
  private String HTML_DIR = "/Users/sorabae/Research/gator-3.3/WTGDebugger";
  private String SEP = File.separator;
  private GUIAnalysisOutput guiOutput;
  private RoboSynthesizer robo;

  @Override
  public void run(GUIAnalysisOutput output) {
    this.guiOutput = output;

    VarUtil.v().guiOutput = output;
    WTGBuilder wtgBuilder = new WTGBuilder();
    wtgBuilder.build(output);
    WTGAnalysisOutput wtgAO = new WTGAnalysisOutput(output, wtgBuilder);
    WTG wtg = wtgAO.getWTG();
    System.out.println("* calculate window transition graph.");

		try {
      File f = new File(HTML_DIR + SEP + "subgraph.json");
      JSONParser parser = new JSONParser();
      JSONObject json = (JSONObject) parser.parse(new FileReader(f));
      synthesis(wtg, json);
      System.out.println("* success to synthesize a program");
		} catch (Exception e) {
      System.out.println("* fail to synthesize a program");
      e.printStackTrace(System.out);
    }
  }

  public void synthesis(WTG wtg, JSONObject json) {
    robo = new RoboSynthesizer(guiOutput.getAppPackageName());

    // collect method names
    List<String> names = new ArrayList();

    if (Configs.subgraph) {
      // synthesize for a subgraph
      // find out the source node
      JSONArray edges = (JSONArray) json.get("edges");
      if (edges.isEmpty()) return;
      JSONObject edge = (JSONObject) edges.get(0);
      WTGNode source = wtg.getNode(Integer.parseInt((String) edge.get("source")));
      assert(source != null);

      names.add(source.getWindow().getClassType().getShortName());
      start(wtg, source);
    } else {
      // synthesize each subgraph starting from each node
      JSONArray nodes = (JSONArray) json.get("nodes");
      Iterator it = nodes.iterator();
      while (it.hasNext()) {
        JSONObject node = (JSONObject) it.next();
        WTGNode source = wtg.getNode(Integer.parseInt((String) node.get("id")));
        assert(source != null);

        names.add(source.getWindow().getClassType().getShortName());
        start(wtg, source);
      }
    }

    // set up template parameters first
    Velocity.init();
    VelocityContext context = new VelocityContext();
    SootClass mainActivityClass = guiOutput.getMainActivity();
    context.put("package", guiOutput.getAppPackageName());
    context.put("activity", mainActivityClass.getShortName());
    if (8 > Configs.getAndroidAPILevel()) {
      context.put("init", "\"" + guiOutput.getAppPackageName() + "\", " + mainActivityClass.getShortName() + ".class");
    } else {
      context.put("init", mainActivityClass.getShortName() + ".class");
    }
    context.put("activity_whole_path", mainActivityClass.getPackageName() + "." + mainActivityClass.getShortName());

    // write down the synthesized programs on a file using a template
    genCaseFile(context, "SynthesizedProgram", names, robo);
  }

  public void start(WTG wtg, WTGNode source) {
    // collect edges for this subgraph, and
    // sort into those having the same target node
    HashMap<WTGNode, List<WTGEdge>> edges = new HashMap();
    for (WTGEdge edge : source.getOutEdges()) {
      WTGNode key = edge.getTargetNode();
      List<WTGEdge> value = edges.get(key);
      if (value == null) {
        value = new ArrayList();
      }
      value.add(edge);
      edges.put(key, value);
    }
    if (edges.isEmpty()) return;

    // collect nodes for this subgraph
    List<WTGNode> nodes = new ArrayList(edges.keySet());
    if (!nodes.contains(source)) {
      nodes.add(source);
    }
    List<SootClass> windows = nodes.stream().map(node -> node.getWindow().getClassType()).collect(Collectors.toList());

    System.out.println("Synthesize a node (id: " + source.getId() + ", name: " + source.getWindow().getClassType().getShortName() + ")");
    // start synthesis
    robo.synthesizeProgram(source, edges, windows);
  }

  /**
   * Generate test case file in Android JUnit format.
   */
  private void genCaseFile(VelocityContext context, String className, List<String> methodNames,
                           RoboSynthesizer robo) {
    context.put("classname", className);
    context.put("methodnames", methodNames);
    context.put("test_list", robo.casesAsStrings());
    context.put("import_list", robo.imports);
    context.put("helper_list", robo.helpers);
    context.put("global_list", robo.globals);
    context.put("helper_classes", robo.helperClasses);
    context.put("helper_objects", robo.helperObjects);
    context.put("setup_list", robo.setups);
    try {
      File f = new File(HTML_DIR + SEP + "program.java");
      BufferedWriter writer = new BufferedWriter(new FileWriter(f));
      Velocity.evaluate(context, writer, "Robo Test Case Template", HelperDepot.template);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
