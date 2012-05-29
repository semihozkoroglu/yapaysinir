import java.io.*;

public class fileread{
   
  public static int linecount;
  public static String[][] all;
  
  public static void split(String f) throws Exception{

    File file = null;
    FileReader freader = null;
    LineNumberReader lnreader = null;
    int i = 0,j;
    linecount = count(f);//ornek sayımız
    all = new String[linecount][];
    
    try{
        file = new File(f);
        freader = new FileReader(file);
        lnreader = new LineNumberReader(freader);
        String line = "";
        while ((line = lnreader.readLine()) != null){
            all[i++] = line.split(" ");
        }
        freader.close();
        lnreader.close();
    }
    catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
    }
  }
  public static Integer count(String file){
    
    try {  
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int lines = 0;

        while (reader.readLine() != null) lines++;
        
        reader.close();

        return lines;
    }catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        return -1;
    }
  }
}