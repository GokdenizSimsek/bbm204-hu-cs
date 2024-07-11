import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        /* YOUR CODE HERE */

        List<Molecule> molecules = new ArrayList<>();

        for (MolecularStructure humanStructure : humanStructures) {
            Molecule mol = humanStructure.getMoleculeWithWeakestBondStrength();
            molecules.add(mol);
        }
        for (MolecularStructure diffStructure : diffStructures) {
            Molecule mol = diffStructure.getMoleculeWithWeakestBondStrength();

            boolean hasMolecule = false;
            for (Molecule molecule : molecules) {
                if (molecule.getId().equals(mol.getId())) {
                    hasMolecule = true;
                    break;
                }
            }
            if (!hasMolecule) {
                molecules.add(mol);
            }
        }
        System.out.println(molecules.size());

        for (int i = 0; i < molecules.size(); i++) {
            HashMap<String,Double> differences = new HashMap<>();
            List<String> keysList = new ArrayList<>();
            for (int j = 0; j < molecules.size(); j++) {
                if (!(i == j)) {
                    keysList.add(molecules.get(j).getId());
                    double bondLength = (double) (molecules.get(i).getBondStrength() + molecules.get(j).getBondStrength()) /2;
                    differences.put(molecules.get(j).getId(), bondLength);
                }
            }

            String smallestKey = null;
            double smallestValue = Double.MAX_VALUE;
            for (String keys:keysList) {
                double value = differences.get(keys);

                if (value < smallestValue) {
                    smallestValue = value;
                    smallestKey = keys;
                }
            }

            for (Molecule molecule:molecules) {
                if (molecule.getId().equals(smallestKey)) {
                    Bond bond = new Bond(molecule,molecules.get(i),smallestValue);
                    boolean hasBond = false;
                    for (Bond bonds:serum) {
                        if (bonds.getFrom() == bond.getTo() && bonds.getTo() == bond.getFrom()) {
                            hasBond = true;
                            break;
                        }
                    }
                    if (!hasBond) {
                        serum.add(bond);
                    }
                }
            }
        }

        return serum;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {

        /* YOUR CODE HERE */

        List<Molecule> molecules = new ArrayList<>();

        System.out.print("Typical human molecules selected for synthesis: [");
        for (int j = 0; j < humanStructures.size(); j++) {
            int indexMinBond = 0;
            for (int i = 1; i < humanStructures.get(j).getMolecules().size(); i++) {
                if (humanStructures.get(j).getMolecules().get(indexMinBond).getBondStrength() > humanStructures.get(j).getMolecules().get(i).getBondStrength()){
                    indexMinBond = i;
                }
            }
            if (j == 0) {
                System.out.print(humanStructures.get(j).getMolecules().get(indexMinBond).getId());
            } else {
                System.out.printf(", %s",humanStructures.get(j).getMolecules().get(indexMinBond).getId());
            }
            molecules.add(humanStructures.get(j).getMolecules().get(indexMinBond));
        }
        System.out.print("]\nVitales molecules selected for synthesis: [");
        boolean everWritted = false;
        for (MolecularStructure diffStructure : diffStructures) {
            int indexMinBond = 0;
            for (int i = 1; i < diffStructure.getMolecules().size(); i++) {
                if (diffStructure.getMolecules().get(indexMinBond).getBondStrength() > diffStructure.getMolecules().get(i).getBondStrength()) {
                    indexMinBond = i;
                }
            }
            boolean hasMolecule = false;

            for (Molecule molecule : molecules) {
                if (molecule.getId().equals(diffStructure.getMolecules().get(indexMinBond).getId())) {
                    hasMolecule = true;
                    break;
                }
            }
            if (!hasMolecule) {
                molecules.add(diffStructure.getMolecules().get(indexMinBond));
                if (!everWritted) {
                    System.out.print(diffStructure.getMolecules().get(indexMinBond).getId());
                } else {
                    System.out.printf(", %s", diffStructure.getMolecules().get(indexMinBond).getId());
                }
                everWritted = true;
            }
        }
        System.out.print("]\nSynthesizing the serum...\n");

        double bondSum = 0;
        for (Bond bond:serum) {
            if (bond.getFrom().compareTo(bond.getTo()) < 0) {
                System.out.printf("Forming a bond between %s - %s with strength %.2f\n", bond.getFrom(), bond.getTo(), bond.getWeight());
            } else {
                System.out.printf("Forming a bond between %s - %s with strength %.2f\n", bond.getTo(), bond.getFrom(), bond.getWeight());
            }
            bondSum += bond.getWeight();
        }
        System.out.printf("The total serum bond strength is %.2f\n", bondSum);
    }
}
