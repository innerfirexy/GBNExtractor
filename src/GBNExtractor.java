import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class GBNExtractor {
    protected String inputFileName;
    protected Map<String, Integer> vocab;
    protected Map<String, String> results;

    GBNExtractor(String filename) {
        inputFileName = filename;
        vocab = new TreeMap<String, Integer>();
        results = new TreeMap<String, String>();
    }

    public void getFirstYear() {
        int count = 0;
        try {
           BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
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
               if (vocab.containsKey(items[0])) {
                   continue;
               }
               vocab.put(items[0], 1);

               // Remove POS tag part in the word
               String pattern = ".*\\_[A-Z]+$";
               if (Pattern.matches(pattern, items[0])) {
                   String word = items[0].replaceAll("\\_[A-Z]+$", "");
                   results.put(word, items[1]);
               } else {
                   results.put(items[0], items[1]);
               }
           }
           reader.close();
        } catch (Exception e) {
           System.err.format("Exception occurred trying to read '%s'.", inputFileName);
           e.printStackTrace();
        }
    }

    public void saveRecords(String outputFileName) {
        assert !results.isEmpty();
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_8));
            for (Map.Entry<String, String> entry: results.entrySet()) {
                String word = entry.getKey();
                String year = entry.getValue();
                writer.write(word + " " + year + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.err.format("Exception occurred trying to write to '%s'.", outputFileName);
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        for (char i = 'a'; i <= 'z'; i++) {
            String filename = "/Data/GoogleBooksNgram/English/googlebooks-eng-all-1gram-20120701-" + i;
            System.out.println("Processing " + filename);
            String outputname="first_year_" + i;

            GBNExtractor ex1 = new GBNExtractor(filename);
            ex1.getFirstYear();
            ex1.saveRecords(outputname);
        }
//        GBNExtractor ex1 = new GBNExtractor("/Data/GoogleBooksNgram/English/googlebooks-eng-all-1gram-20120701-a");
//        GBNExtractor ex1 = new GBNExtractor("/Data/GoogleBooksNgram/English/test_a");
//        ex1.getFirstYear();
//        ex1.saveRecords("/Data/GoogleBooksNgram/English/test_a_fy_java");
    }
}
