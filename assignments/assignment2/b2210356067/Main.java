import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {

        /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");

        ArrayList<Integer> data = new ArrayList<>();
        int sum = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line = reader.readLine();
            String[] splitWords = line.split(" ");
            for (String datas: splitWords) {
                data.add(Integer.parseInt(datas));
                sum += Integer.parseInt(datas);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        PowerGridOptimization optimization = new PowerGridOptimization(data);
        OptimalPowerGridSolution solution = optimization.getOptimalPowerGridSolutionDP();

        System.out.println("The total number of demanded gigawatts: " + sum);
        System.out.println("Maximum number of satisfied gigawatts: " + solution.getmaxNumberOfSatisfiedDemands());
        System.out.print("Hours at which the battery bank should be discharged: ");
        for (int i = 0; i < solution.getHoursToDischargeBatteriesForMaxEfficiency().size(); i++){
            System.out.print(solution.getHoursToDischargeBatteriesForMaxEfficiency().get(i));
            if (i != solution.getHoursToDischargeBatteriesForMaxEfficiency().size() - 1){
                System.out.print(", ");
            }
        }
        System.out.println("\nThe number of unsatisfied gigawatts: " + (sum - solution.getmaxNumberOfSatisfiedDemands()));

        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");

        ArrayList<Integer> data2 = new ArrayList<>();
        int energyCapacity;
        int numberOfESV;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[1]));
            String line = reader.readLine();
            String[] splitWords1 = line.split(" ");
            numberOfESV = Integer.parseInt(splitWords1[0]);
            energyCapacity = Integer.parseInt(splitWords1[1]);

            line = reader.readLine();
            String[] splitWords2 = line.split(" ");
            for (String datas: splitWords2) {
                data2.add(Integer.parseInt(datas));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        OptimalESVDeploymentGP optimalESV = new OptimalESVDeploymentGP(data2);
        int solution2 = optimalESV.getMinNumESVsToDeploy(numberOfESV,energyCapacity);
        ArrayList<ArrayList<Integer>> ESVArray = optimalESV.getMaintenanceTasksAssignedToESVs();
        if (solution2 != -1) {
            System.out.println("The minimum number of ESVs to deploy: " + solution2);
            int ESVNum = 1;
            for (ArrayList<Integer> array: ESVArray) {
                System.out.printf("ESV %d tasks: [", ESVNum);
                for (int i = 0; i < array.size(); i++){
                    System.out.print(array.get(i));
                    if (i != array.size() - 1){
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
                ESVNum++;
            }
        } else {
            System.out.println("Warning: Mission Eco-Maintenance Failed.");
        }
        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
}
