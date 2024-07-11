import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Quiz1 {
    public static void main(String[] args) throws IOException {
        // TODO: Use the first command line argument (args[0]) as the file to read the input from
        // TODO: Your code goes here
        // TODO: Print the solution to STDOUT
        //String fileName = args[0];
        String fileName = "src/input.txt";
        ArrayList<String> ignoredWords = new ArrayList<>();
        ArrayList<String> titleWords = new ArrayList<>();
        ArrayList<ArrayList<String>> poetRows = new ArrayList<>();
        boolean ignoreWordsDone = false;

        Comparator<String> caseInsensitiveComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        };

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("...")) {
                    ignoreWordsDone = true;
                }
                if (!ignoreWordsDone) {
                    ignoredWords.add(line);
                } else {
                    ArrayList<String> row = new ArrayList<>();
                    String[] splitted = line.split(" ");
                    for (String word : splitted) {
                        row.add(word);
                    }
                    poetRows.add(row);
                }
            }
            for (ArrayList<String> lines : poetRows) {
                for (String word : lines) {
                    if (word.charAt(0) == 'I') {
                        word = "i" + word.substring(1);
                    }
                    if (!ignoredWords.contains(word.toLowerCase())) {
                        titleWords.add(word);
                    }
                }
            }
            Collections.sort(titleWords, caseInsensitiveComparator);
            for (String word : titleWords) {
                word.replace("i", "ı");
                for (ArrayList<String> row : poetRows) {
                    if (row.contains(word)) {
                        int index = row.indexOf(word);
                        for (int i = 0; i < row.size(); i++) {
                            if (index == i) {
                                System.out.print(row.get(i).toUpperCase() + " ");
                            } else {
                                System.out.print(row.get(i).toLowerCase() + " ");
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}