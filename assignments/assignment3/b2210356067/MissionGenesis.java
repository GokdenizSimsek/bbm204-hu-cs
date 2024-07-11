// Class representing the mission of Genesis
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {

        /* YOUR CODE HERE */

        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList humanMoleculeList = doc.getElementsByTagName("HumanMolecularData");
            NodeList vitalesMoleculeList = doc.getElementsByTagName("VitalesMolecularData");

            // Parse human molecular data
            if (humanMoleculeList.getLength() > 0) {
                molecularDataHuman = parseMolecularData(humanMoleculeList.item(0));
            }


            // Parse Vitales molecular data
            if (vitalesMoleculeList.getLength() > 0) {
                molecularDataVitales = parseMolecularData(vitalesMoleculeList.item(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to parse MolecularData from a NodeList
    private MolecularData parseMolecularData(Node node) {
        List<Molecule> molecules = new ArrayList<>();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            NodeList moleculeNodes = ((Element) node).getElementsByTagName("Molecule");
            for (int i = 0; i < moleculeNodes.getLength(); i++) {
                Element moleculeElement = (Element) moleculeNodes.item(i);
                String moleculeId = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());
                List<String> bonds = new ArrayList<>();
                NodeList bondNodes = moleculeElement.getElementsByTagName("MoleculeID");
                for (int j = 0; j < bondNodes.getLength(); j++) {
                    bonds.add(bondNodes.item(j).getTextContent());
                }
                Molecule molecule = new Molecule(moleculeId, bondStrength, bonds);
                molecules.add(molecule);
            }
        }
        return new MolecularData(molecules);
    }
}
