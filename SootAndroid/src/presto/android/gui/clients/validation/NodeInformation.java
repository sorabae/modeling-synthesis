package presto.android.gui.clients.validation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import presto.android.Hierarchy;
import presto.android.gui.PropertyManager;
import presto.android.gui.graph.*;
import presto.android.gui.wtg.ds.WTGEdge;
import presto.android.gui.wtg.ds.WTGNode;
import presto.android.gui.wtg.flowgraph.NLauncherNode;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import java.util.*;

/**
 * Helpers' code warehouse.
 */
public class NodeInformation {
  private int id;
  private String type;
  private String name;
  private String raw_name;

  public NodeInformation (WTGNode node) {
    id = node.getId();
    type = parseType(node);
    name = parseName(node);
    raw_name = node.getWindow().toString();
  }

  private String parseType (WTGNode node) {
    String window = node.getWindow().toString();

    window = window.replaceAll("\\[.*", "");
    return window;
  }

  private String parseName (WTGNode node) {
    String window = node.getWindow().toString();

    window = window.replaceAll(".*\\[", "");
    window = window.replaceAll("\\].*", "");
    return window;
  }

  public int getId () {
    return this.id;
  }

  public String getType () {
    return this.type;
  }

  public String getName () {
    return this.name;
  }

  public String getRawName () {
    return this.raw_name;
  }
}
