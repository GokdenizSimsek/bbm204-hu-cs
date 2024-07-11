import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        List<MolecularStructure> structures = new ArrayList<>();

        // Set to keep track of processed molecules
        Set<Molecule> processed = new HashSet<>();

        int haveBondsInt = 0;
        for (Molecule molecule : molecules) {
            if (!processed.contains(molecule)) {
                boolean haveBonds = false;
                for (int i = 0; i < structures.size(); i++) {
                    for (Molecule molecule1:structures.get(i).getMolecules()) {
                        if(molecule.getBonds().contains(molecule1.getId())){
                            haveBonds = true;
                            haveBondsInt = i;
                        }
                    }
                }
                if (!haveBonds) {
                    MolecularStructure structure = new MolecularStructure();
                    exploreStructure(molecule, structure, processed);
                    structures.add(structure);
                } else {
                    exploreStructure(molecule, structures.get(haveBondsInt), processed);
                }
            }
        }

        return structures;
    }

    // Helper method to recursively explore molecular structures
    private void exploreStructure(Molecule molecule, MolecularStructure structure, Set<Molecule> processed) {
        // Add current molecule to the structure
        structure.addMolecule(molecule);
        processed.add(molecule);

        // Explore bonds of current molecule
        for (String bondId : molecule.getBonds()) {
            // Find the connected molecule
            Molecule connectedMolecule = molecules.stream()
                    .filter(m -> m.getId().equals(bondId))
                    .findFirst().orElse(null);
            // If connected molecule is found and not already processed, recursively explore it
            if (connectedMolecule != null && !processed.contains(connectedMolecule)) {
                exploreStructure(connectedMolecule, structure, processed);
            }
        }
    }

    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {

        /* YOUR CODE HERE */

        System.out.printf("%d molecular structures have been discovered in %s.\n",molecularStructures.size(), species);
        for (int i = 0; i < molecularStructures.size(); i++) {
            System.out.printf("Molecules in Molecular Structure %d: [", i+1);
            Collections.sort(molecularStructures.get(i).getMolecules());
            for (int j = 0; j < molecularStructures.get(i).getMolecules().size(); j++) {
                if (j == 0) {
                    System.out.print(molecularStructures.get(i).getMolecules().get(j).getId());
                } else {
                    System.out.printf(", %s",molecularStructures.get(i).getMolecules().get(j).getId());
                }
            }
            System.out.print("]\n");
        }

    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targeStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();

        /* YOUR CODE HERE */

        for (int i = 0; i < sourceStructures.size(); i++) {
            if (!sourceStructures.get(i).equals(targeStructures.get(i))) {
                anomalyList.add(targeStructures.get(i));
            }
        }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        /* YOUR CODE HERE */

        if (molecularStructures.size() > 0) {
            System.out.print("Molecular structures unique to Vitales individuals:\n");
            for (int i = 0; i < molecularStructures.size(); i++) {
                System.out.print("[");
                for (int j = 0; j < molecularStructures.get(i).getMolecules().size(); j++) {
                    if (j == molecularStructures.get(i).getMolecules().size() - 1) {
                        System.out.printf("%s]\n", molecularStructures.get(i).getMolecules().get(j).getId());
                    } else {
                        System.out.printf("%s, ", molecularStructures.get(i).getMolecules().get(j).getId());
                    }
                }
            }
        }

    }
}
