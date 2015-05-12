import java.awt.*;
import java.applet.*;
import java.io.*;

public class IO {
  private static final int SYSTEM = 0;
  private static final int BATCH  = 1;
  
  private int outType = SYSTEM;
  private int inType  = SYSTEM;
  
  //private DataInputStream inStream;
  private BufferedReader inStream;
  private PrintStream outStream;
  private String inString = ""; 
  
  public IO (InputStream in, PrintStream out) {
    inType = SYSTEM; 
    outType = SYSTEM; 
    //inStream = new DataInputStream(in);
    inStream = new BufferedReader(new InputStreamReader(in));
    outStream = out;
  }

  public IO (String inName, PrintStream out) {
    try {
      inType = BATCH;
      outType = SYSTEM;
      FileInputStream in1 = new FileInputStream(inName);
      // inStream = new DataInputStream(in1);
      inStream = new BufferedReader(new InputStreamReader(in1));
      outStream = out; 
    } catch (Exception e) {}
  }

  public void print(String s) {
    switch (outType) {
      case SYSTEM : outStream.print(s);
                    outStream.flush();
                    break;
    }
  }

  public void println(String s) {
    print(s+"\n");
  }

  public String getLine(int inType) {
  String s = "ERROR";
    try {
      switch (inType) {
        case BATCH  : s = inStream.readLine();
                      println(s);
                      break; 
        case SYSTEM : s = inStream.readLine();
                      break;
      }
    } catch (Exception e) {}
    return s;
  }
  
  public String getLine() {
    return getLine(inType);
  }

  public static int getInt(String s, int loc) throws NumberFormatException {
    s = s.substring(loc);
    s = s.trim(); 
    int i = Integer.parseInt(s);
    return i;
  }

  public int getInt() throws NumberFormatException {
  String substr;
    if (inString.length() == 0) inString = getLine(SYSTEM);
    inString = inString.trim(); 
    int i = inString.indexOf(' ');
    //System.out.println("inString["+inString+"]");
    //System.out.println("i="+i);
    if (i < 0) {
      substr = inString;
      inString = "";
    } else {
      substr = inString.substring(0,i);
      inString = inString.substring(i+1);
    }
    //System.out.println("substr["+substr+"]");
    return getInt(substr,0);
  } 
  
  public void showVal(Object val, int w) {
    String s;
    if (val == null)
      s = "null";
    else
      s = val.toString();
    for (int j=s.length()+1; j<=w; j++)
      print(" ");
    print(s);
  }
 
}

