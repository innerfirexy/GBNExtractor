import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class GBNExtractor {
    protected HashMap<String, Integer> records;
    protected HashMap<String, String> results;

    GBNExtractor() {
        records = new HashMap<String, Integer>();
        results = new HashMap<String, String>();
    }

    public void getFirstYear(String filename) {
        int count = 0;
        try {
           BufferedReader reader = new BufferedReader(new FileReader(filename));
           String line;
           while (true) {
               line = reader.readLine();
               if (line == null) {
                   break;
               }
               count += 1;
               if (count % 10000 == 0) {
                   System.out.print(String.format("\r%d lines processed", count));
                   System.out.flush();
               }

               String[] items = line.split("\t");
               if (records.containsKey(items[0])) {
                   continue;
               }
               records.put(items[0], 1);
               results.put(items[0], items[1]);
           }
        } catch (Exception e) {
           System.err.format("Exception occurred trying to read '%s'.", filename);
           e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        GBNExtractor ex1 = new GBNExtractor();

        ex1.getFirstYear("/Data/GoogleBooksNgram/English/googlebooks-eng-all-1gram-20120701-a");
    }
}
