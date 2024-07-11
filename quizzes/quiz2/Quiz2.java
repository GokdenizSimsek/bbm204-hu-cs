import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Quiz2 {
    static final int MAX_CAPACITY = 10000;
    static final int MAX_RESOURCES = 300;
    static final int MAX_MASS = 100000;

    public static void main(String[] args) {
        String inputFile = args[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            StringTokenizer st = new StringTokenizer(reader.readLine());
            int M = Integer.parseInt(st.nextToken());
            int n = Integer.parseInt(st.nextToken());

            int[] masses = new int[n];
            st = new StringTokenizer(reader.readLine());
            for (int i = 0; i < n; i++) {
                masses[i] = Integer.parseInt(st.nextToken());
            }

            // Solve the problem
            Result result = knapsack(M, masses);

            // Print the output matrix
            System.out.println(result.maxMass);
            for (boolean[] row : result.dp) {
                for (boolean cell : row) {
                    System.out.print(cell ? "1" : "0");
                }
                System.out.println();
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }
    }

    static class Result {
        int maxMass;
        boolean[][] dp;

        Result(int maxMass, boolean[][] dp) {
            this.maxMass = maxMass;
            this.dp = dp;
        }
    }

    static Result knapsack(int M, int[] masses) {
        int n = masses.length;
        M = Math.min(M, MAX_CAPACITY);
        n = Math.min(n, MAX_RESOURCES);

        boolean[][] dp = new boolean[M + 1][n + 1];

        for (int i = 0; i <= n; i++) {
            dp[0][i] = true;
        }

        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= n; j++) {
                if (masses[j - 1] <= i) {
                    dp[i][j] = dp[i][j - 1] || dp[i - masses[j - 1]][j - 1];
                } else {
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }

        int maxMass = 0;
        for (int m = M; m >= 0; m--) {
            if (dp[m][n]) {
                maxMass = m;
                break;
            }
        }

        return new Result(maxMass, dp);
    }
}