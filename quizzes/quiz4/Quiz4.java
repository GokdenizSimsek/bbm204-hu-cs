import java.io.*;
import java.util.*;

public class Quiz4 {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java Quiz4 <database file> <query file>");
            return;
        }

        String databaseFile = args[0];
        String queryFile = args[1];

        // Create a Trie and populate it with data from the database file
        Trie trie = new Trie();
        populateTrie(trie, databaseFile);

        // Process queries from the query file
        processQueries(trie, queryFile);
    }

    private static void populateTrie(Trie trie, String databaseFile) throws IOException {
        BufferedReader dbReader = new BufferedReader(new FileReader(databaseFile));
        int n = Integer.parseInt(dbReader.readLine().trim());
        for (int i = 0; i < n; i++) {
            String[] line = dbReader.readLine().split("\t");
            long weight = Long.parseLong(line[0]);
            String result = line[1].toLowerCase();
            trie.insert(result, weight);
        }
        dbReader.close();
    }

    private static void processQueries(Trie trie, String queryFile) throws IOException {
        BufferedReader queryReader = new BufferedReader(new FileReader(queryFile));
        String line;
        while ((line = queryReader.readLine()) != null) {
            String[] parts = line.split("\t");
            String query = parts[0].toLowerCase();
            int limit = Integer.parseInt(parts[1]);
            List<Result> results = trie.search(query, limit);
            System.out.println("Query received: \"" + query + "\" with limit " + limit + ". Showing results:");
            if (results.isEmpty()) {
                System.out.println("No results.");
            } else {
                for (Result result : results) {
                    System.out.println("- " + result.weight + " " + result.word);
                }
            }
        }
        queryReader.close();
    }
}

class TrieNode {
    Map<Character, TrieNode> children;
    List<Result> results;

    TrieNode() {
        children = new HashMap<>();
        results = new ArrayList<>();
    }
}

class Trie {
    private TrieNode root;

    Trie() {
        root = new TrieNode();
    }

    void insert(String word, long weight) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
            current.results.add(new Result(word, weight));
            Collections.sort(current.results, (a, b) -> Long.compare(b.weight, a.weight));
        }
    }

    List<Result> search(String prefix, int limit) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return new ArrayList<>();
            }
            current = current.children.get(c);
        }
        if (limit == 0) {
            return new ArrayList<>();
        }
        return current.results.subList(0, Math.min(limit, current.results.size()));
    }
}

class Result {
    String word;
    long weight;

    Result(String word, long weight) {
        this.word = word;
        this.weight = weight;
    }
}