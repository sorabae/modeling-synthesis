package presto.android.gui.clients.synthesis;

public class Util {
  public static String prepend(String input, String stuff) {
    String[] str = input.split("\n");
    StringBuffer sb = new StringBuffer();
    for (String s : str)
      sb.append(stuff + s + "\n");
    return sb.toString();
  }
}
