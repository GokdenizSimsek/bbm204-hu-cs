import java.util.ArrayList;
import java.util.Collections;

public class OptimalESVDeploymentGP {
    private ArrayList<Integer> maintenanceTaskEnergyDemands;
    private ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = new ArrayList<>();

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
    }

    public ArrayList<Integer> getMaintenanceTaskEnergyDemands() {
        return maintenanceTaskEnergyDemands;
    }

    public ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {
        return maintenanceTasksAssignedToESVs;
    }

    public int getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) {
        // Sort maintenance task energy demands in decreasing order
        Collections.sort(maintenanceTaskEnergyDemands, Collections.reverseOrder());

        // Initialize remaining energy capacities in all available ESVs
        ArrayList<Integer> remainingCapacities = new ArrayList<>();
        for (int i = 0; i < maxNumberOfAvailableESVs; i++) {
            remainingCapacities.add(maxESVCapacity);
        }

        // Iterate through each maintenance task
        for (Integer energyDemand : maintenanceTaskEnergyDemands) {
            boolean taskAssigned = false;
            // Find the first ESV that can accommodate the task
            for (int i = 0; i < maxNumberOfAvailableESVs; i++) {
                if (remainingCapacities.get(i) >= energyDemand) {
                    // Task fits in this ESV
                    if (maintenanceTasksAssignedToESVs.size() <= i) {
                        maintenanceTasksAssignedToESVs.add(new ArrayList<>());
                    }
                    maintenanceTasksAssignedToESVs.get(i).add(energyDemand);
                    remainingCapacities.set(i, remainingCapacities.get(i) - energyDemand);
                    taskAssigned = true;
                    break;
                }
            }
            // Get a new ESV only if the task does not fit in any of the already used ESVs
            if (!taskAssigned) {
                if (remainingCapacities.size() < maxNumberOfAvailableESVs) {
                    remainingCapacities.add(maxESVCapacity - energyDemand);
                    ArrayList<Integer> newESV = new ArrayList<>();
                    newESV.add(energyDemand);
                    maintenanceTasksAssignedToESVs.add(newESV);
                } else {
                    return -1; // All tasks could not be accommodated
                }
            }
        }
        // Return the minimum number of ESVs required
        return maintenanceTasksAssignedToESVs.size();
    }
}