package presto.android.gui.clients;

import com.google.common.collect.Lists;
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
import java.util.ArrayList;
import java.util.Comparator;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class HTMLDemoClient implements GUIAnalysisClient {
  private String HTML_DIR = "/Users/cce13/dev/modeling-synthesis/WTGDebugger";

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

      WTGtoJSON(wtg);
      System.out.println("* success to write a JSON file");
		} catch (Exception e) {
      System.out.println("* fail to write a HTML file");
      //throw e;
    }

    for (WTGNode node : wtg.getNodes()) {
      System.out.print("[WTGNode] window: ");
      System.out.println(node.getWindow());
      System.out.print("          type:   ");
      System.out.println(node.getWindow().getClass().getName());
    }

    Velocity.init();
    VelocityContext context = new VelocityContext();

    Gson gson = new Gson();
    System.out.println(gson.toJson(context));
    System.out.println("context printed");
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
    "\t\t<script src='testgen.js' type='text/javascript'></script>\n" +
    "\t\t<script src='core.js' type='text/javascript'></script>\n" +
    "\t</head>\n" +
    "\t<body>\n" +
    "\t\t<div class='container-fluid'>\n" +
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
    "\t\t\t\t\t<div id='cy' style='position: relative;'></div>\n" +
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
    "\t\t\t\t\t<div id='cy_part' style='position: relative;'></div>\n" +
    "\t\t\t\t</div>\n" +
    "\t\t\t</div>\n" +
    "\t\t</div>\n");

    // Print tables describe components of WTG
    sb.append(getWTGTable(wtg));
    
    sb.append(
    "\t</body>\n" +
    "\t<script>\n" +
    "\t</script>\n" +
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

  public String getWTGTable(WTG wtg) {
    StringBuilder sb = new StringBuilder();

    sb.append(
      "\t\t<div class='container-fluid' style='position: relative;'>\n" +
      "\t\t\t<table class='table'>\n" +
      "\t\t\t\t<thead>\n" +
      "\t\t\t\t\t<tr>\n" + 
      "\t\t\t\t\t\t<th>ID</th>\n" +
      "\t\t\t\t\t\t<th>Type</th>\n" +
      "\t\t\t\t\t\t<th>Name</th>\n" +
      "\t\t\t\t\t</tr>\n" +
      "\t\t\t\t</thead>\n" +
      "\t\t\t\t<tbody>\n"
      );
  
      ArrayList<WTGNode> nodeList = new ArrayList<WTGNode>();
      NodeComparator cmp = new NodeComparator();
  
      for (WTGNode node : wtg.getNodes()) {
        nodeList.add(node);
      }
      nodeList.sort(cmp);
  
      nodeList.forEach(node -> sb.append(
        "\t\t\t\t\t<tr>\n" +
        "\t\t\t\t\t\t<td>" + node.getId() + "</td>\n" +
        "\t\t\t\t\t\t<td>" + parseType(node) + "</td>\n" +
        "\t\t\t\t\t\t<td>" + parseName(node) + "</td>\n" +
        "\t\t\t\t\t</tr>\n"
        ));
  
      sb.append(
        "\t\t\t\t</tbody>\n" +
        "\t\t\t</table>\n" +
        "\t\t</div>"
      );
  
      sb.append(
        "\t\t<hr>\n" + 
        "\t\t<div class='container-fluid' style='position: relative;'>\n" +
        "\t\t<table class='table'>\n" +
        "\t\t\t<thead>\n" +
        "\t\t\t\t<tr>\n" + 
        "\t\t\t\t\t<th>Source</th>\n" +
        "\t\t\t\t\t<th>Target</th>\n" +
        "\t\t\t\t\t<th>Root</th>\n" +
        "\t\t\t\t\t<th>WTG Handlers</th>\n" +
        "\t\t\t\t\t<th>Callbacks</th>\n" +
        "\t\t\t\t</tr>\n" +
        "\t\t\t</thead>\n" +
        "\t\t\t<tbody>\n"
      );
  
      for (WTGEdge edge : wtg.getEdges()) {
        sb.append(
          "\t\t\t\t<tr>\n" +
          "\t\t\t\t\t<td>" + edge.getSourceNode().getId() + "</td>\n" +
          "\t\t\t\t\t<td>" + edge.getTargetNode().getId() + "</td>\n" +
          "\t\t\t\t\t<td>" + edge.getRootTag().toString() + "</td>\n"
        );
  
        int count = 0;
        sb.append(
          "\t\t\t\t\t<td>\n"
        );
        for (EventHandler handler : edge.getWTGHandlers()) {
          String handlerInfo = handler.toString();
          handlerInfo = handlerInfo.replaceAll("<", "&lt;");
          handlerInfo = handlerInfo.replaceAll(">", "&gt;");
          sb.append("\t\t\t\t\t\t" + count++ + ": " + handlerInfo + "<br>\n");
        }
        sb.append(
          "\t\t\t\t\t</td>\n"
        );
  
        count = 0;
        sb.append(
          "\t\t\t\t\t<td>\n"
        );
        for (EventHandler callback : edge.getCallbacks()) {
          String callbackInfo = callback.toString();
          callbackInfo = callbackInfo.replaceAll("<", "&lt;");
          callbackInfo = callbackInfo.replaceAll(">", "&gt;");
          sb.append("\t\t\t\t\t\t" + count++ + ": " + callbackInfo + "<br>\n");
        }
        sb.append(
          "\t\t\t\t\t</td>\n" +
          "\t\t\t\t</tr>\n"
        );
      }
  
      sb.append(
        "\t\t\t</tbody>\n" +
        "\t\t</table>\n" +
        "\t\t</div>\n"
      );
  
      sb.append(
        "\t\t<hr>\n" +
        "\t\t<div class='container-fluid'>\n" +
        "\t\t\t<div class='header clearfix::after'>\n" +
        "\t\t\t\t<h3 class='text-muted'>Test Generator</h3>\n" +
        "\t\t\t</div>\n" +
        "\t\t\t<div class='row graph'>\n" +
        "\t\t\t\t<div class='col-lg-6' style='padding-bottom: 1rem;'>\n" + 
        "\t\t\t\t\t<div id='cy_test_select' style='position: relative; height: 400px; outline: red; outline-style: auto;'></div>\n" +
        "\t\t\t\t</div>\n" +
        "\t\t\t\t<div class='col-lg-6'>\n" + 
        "\t\t\t\t\t<div id='cy_test_path' style='position: relative; height: 400px; outline: blue; outline-style: auto;'></div>\n" +
        "\t\t\t\t</div>\n" +
        "\t\t\t</div>\n" +
        "\t\t</div>\n"
      );

      sb.append(
        "\t\t<button type='button' class='btn btn-primary' id='generate_test'>Generate test</button>\n"
      );

    return sb.toString();
  }

  public void WTGtoJSON(WTG wtg) {
    ArrayList<WTGNode> nodeList = new ArrayList<WTGNode>();
    NodeComparator cmp = new NodeComparator();

    File wtgJSON;
    FileWriter wtgWriter = null;
    try {
      String SEP = File.separator;
      wtgJSON = new File(HTML_DIR + SEP + "wtg.json");
      wtgWriter = new FileWriter(wtgJSON, false);
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (WTGNode node : wtg.getNodes())
      nodeList.add(node);

    nodeList.sort(cmp);

    writeJSON(wtgWriter, "{", 0);
    writeJSON(wtgWriter, "\"nodes\": [", 1);

    for (WTGNode node : nodeList) {
      writeJSON(wtgWriter, "{ \"id\": \"" + node.getId() + "\",", 2);
      writeJSON(wtgWriter, "  \"type\": \"" + parseType(node) + "\",", 2);
      writeJSON(wtgWriter, "  \"classname\": \"" + node.getWindow().getClass().getName() + "\",", 2);
      if (parseType(node).equals(""))
        writeJSON(wtgWriter, "  \"name\": \"" + parseName(node) + "\"}", 2);
      else
        writeJSON(wtgWriter, "  \"name\": \"" + parseName(node) + "\"},", 2);
    }

    writeJSON(wtgWriter, "],", 1);

    writeJSON(wtgWriter, "\"edges\": [", 1);

    for (WTGEdge edge : wtg.getEdges()) {
      writeJSON(wtgWriter, "{ \"src\": \"" + edge.getSourceNode().getId() + "\"," , 2);
      writeJSON(wtgWriter, "\"tgt\": \"" + edge.getTargetNode().getId() + "\"," , 3);
      writeJSON(wtgWriter, "\"tag\": \"" + edge.getRootTag() + "\",", 3);

      int count = 1;
      int len = 0;

      writeJSON(wtgWriter, "\"handlers\": [", 3);
      Set<EventHandler> handlers = edge.getWTGHandlers();
      len = handlers.size();
      for (EventHandler handler : handlers) {
        if (count == len)
          writeJSON(wtgWriter, "\"" + handler.toString() + "\"", 4);
        else
          writeJSON(wtgWriter, "\"" + handler.toString() + "\",", 4);
        count++;
      }
      writeJSON(wtgWriter, "],", 3);

      writeJSON(wtgWriter, "\"callbacks\": [", 3);
      count = 1;
      List<EventHandler> callbacks = edge.getCallbacks();
      len = callbacks.size();
      for (EventHandler callback : callbacks) {
        if (count == len)
          writeJSON(wtgWriter, "\"" + callback.toString() + "\"", 4);
        else
          writeJSON(wtgWriter, "\"" + callback.toString() + "\",", 4);
        count++;
      }
      writeJSON(wtgWriter, "]},", 3);
    }

    writeJSON(wtgWriter, "],", 1);

    writeJSON(wtgWriter, "}", 0);

    try {
      wtgWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return;
  }

  public void writeJSON(FileWriter fw, String content, int tabs) {
    String tabStr = "";
    for (int i=0; i<tabs; i++)
      tabStr += "\t";

    try {
      fw.write(tabStr + content + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String parseName(WTGNode node) {
    return node.getWindow().toString().replaceAll("\\[.*", "");
  }
  
  public String parseType(WTGNode node) {
    return node.getWindow().toString().replaceAll(".*\\[", "").replaceAll("\\].*", "");
  }

  public class NodeComparator implements Comparator<WTGNode>
  {
      public int compare(WTGNode left, WTGNode right) {
          if (left.getId() > right.getId())
              return 1;
          else if (left.getId() == right.getId())
              return 0;
          else
              return -1;
      }
  }
}
