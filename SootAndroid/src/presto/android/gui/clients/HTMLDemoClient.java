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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.io.*;

public class HTMLDemoClient implements GUIAnalysisClient {
  private String HTML_DIR = "/home/cce13st/dev/modeling-synthesis/WTGDebugger";

  @Override
  public void run(GUIAnalysisOutput output) {
    VarUtil.v().guiOutput = output;
    WTGBuilder wtgBuilder = new WTGBuilder();
    wtgBuilder.build(output);
    WTGAnalysisOutput wtgAO = new WTGAnalysisOutput(output, wtgBuilder);
    WTG wtg = wtgAO.getWTG();
    System.out.println("* calculate window transition graph.");

		try {
      String SEP = File.separator;
      File f = new File(HTML_DIR + SEP + "index.html");
      FileWriter fw = new FileWriter(f);
      fw.write(drawGraph(wtg));
      fw.close();
      System.out.println("* success to write a HTML file");
		} catch (Exception e) {
      System.out.println("* fail to write a HTML file");
      //throw e;
    }
  }

  public String drawGraph(WTG wtg) {
    StringBuilder sb = new StringBuilder();

    sb.append(
    "<!DOCTYPE HTML>\n" +
    "<html>\n" +
    "\t<head>\n" +
    "\t\t<title hidden>" + Configs.project + "</title>" +
    "\t\t<meta charset='UTF-8'>\n" +
    "\t\t<link rel='stylesheet' href='css/bootstrap.min.css' type='text/css'>\n" +
    "\t\t<link rel='stylesheet' href='css/style.css' type='text/css'>\n" +
    "\t\t<link rel='stylesheet' href='css/core.css' type='text/css'>\n" +
    "\t\t<script src='jquery-2.0.3.min.js'></script>\n" +
    "\t\t<script src='http://127.0.0.1:8000/socket.io/socket.io.js'></script>\n" +
    "\t\t<script src='cytoscape.min.js'></script>\n" +
    "\t\t<script src='dagre.min.js'></script>\n" +
    "\t\t<script src='cytoscape-dagre.js'></script>\n" +
    "\t\t<script>\n" +
    "\t\t\tvar wtg_DB = { nodes : [\n"
    );

    wtg.getNodes().forEach(node -> sb.append(drawNode(node)));
    sb.append("], edges : [ ");
    wtg.getEdges().forEach(edge -> sb.append(drawEdge(edge)));

    sb.append(
    "\t\t\t]};\n" +
    "\t\t</script>\n" +
    "\t\t<script src='core.js' type='text/javascript'></script>\n" +
    "\t</head>\n" +
    "\t<body>\n" +
    "\t\t<div class='container'>\n" +
    "\t\t\t<div class='header clearfix::after'>\n" +
    "\t\t\t\t<h3 class='text-muted'>WTG Debugger</h3>\n" +
    "\t\t\t</div>\n" +
    "\t\t\t<div class='row graph'>\n" +
    "\t\t\t\t<div class='col-lg-6'>\n" +
    "\t\t\t\t\t<div class='row'>\n" +
    "\t\t\t\t\t\t<div class='col-lg-6'>\n" +
    "\t\t\t\t\t\t\t<h4>WTG</h4>\n" +
    "\t\t\t\t\t\t</div>\n" +
    "\t\t\t\t\t\t<div class='col-lg-6'>\n" +
    "\t\t\t\t\t\t\t<button type='button' class='btn btn-primary synthesis' id='synthesis-all'>Synthesize</button>\n" +
    "\t\t\t\t\t\t</div>\n" +
    "\t\t\t\t\t</div>\n" +
    "\t\t\t\t\t<div id='cy'></div>\n" +
    "\t\t\t\t</div>\n" +
    "\t\t\t\t<div class='col-lg-6'>\n" +
    "\t\t\t\t\t<div class='row'>\n" +
    "\t\t\t\t\t\t<div class='col-lg-6'>\n" +
    "\t\t\t\t\t\t\t<h4>Selected Subgraph</h4>\n" +
    "\t\t\t\t\t\t</div>\n" +
    "\t\t\t\t\t\t<div class='col-lg-6'>\n" +
    "\t\t\t\t\t\t\t<button type='button' class='btn btn-primary synthesis' id='synthesis-part'>Synthesize</button>\n" +
    "\t\t\t\t\t\t</div>\n" +
    "\t\t\t\t\t</div>\n" +
    "\t\t\t\t\t<div id='cy_part'></div>\n" +
    "\t\t\t\t</div>\n" +
    "\t\t\t</div>\n" +
    "\t\t</div>\n" +
    "\t</body>\n" +
    "</html>\n"
    );
    return sb.toString();
  }

  public String drawNode(WTGNode node) {
    return String.format("{ data: { id: %d, content: '%s', border: 2 } },\n", node.getId(), node.toString());
  }

  public String drawEdge(WTGEdge edge) {
    EventType et = edge.getEventType();
    String content = et.toString();
    // if (!et.isImplicit()) {
    //   content = String.format("%s, ", edge.getGUIWidget()) + content;
    // }
    return String.format("{ data: { source: %d, target: %d, content: '%s' } },\n",
      edge.getSourceNode().getId(), edge.getTargetNode().getId(), content);

  }
}
