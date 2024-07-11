import java.util.ArrayList;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){

        int N = amountOfEnergyDemandsArrivingPerHour.size();
        int[] E = new int[N];
        // Fill E array with values from amountOfEnergyDemandsArrivingPerHour
        for (int i = 0; i < N; i++) {
            E[i] = amountOfEnergyDemandsArrivingPerHour.get(i);
        }

        int[] SOL = new int[N + 1];
        ArrayList<Integer>[] HOURS = new ArrayList[N + 1];

        SOL[0] = 0;
        HOURS[0] = new ArrayList<>();

        for (int j = 1; j <= N; j++) {
            int maxVal = Integer.MIN_VALUE;
            int optimalI = -1;

            for (int i = 0; i < j; i++) {
                int tempVal = SOL[i] + Math.min(E[j - 1], (j - i) * (j - i));
                if (tempVal > maxVal) {
                    maxVal = tempVal;
                    optimalI = i;
                }
            }
            SOL[j] = maxVal;

            // Update HOURS array
            HOURS[j] = new ArrayList<>(HOURS[optimalI]);
            HOURS[j].add(j);
        }
        return new OptimalPowerGridSolution(SOL[N], HOURS[N]);
    }
}
