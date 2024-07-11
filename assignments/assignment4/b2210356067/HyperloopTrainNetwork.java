import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        // TODO: Your code goes here

        Pattern p = Pattern.compile("[\\t ]*" + varName + "\\s*=\\s*\"(.*?)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        return m.group(1);
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        // TODO: Your code goes here

        Pattern p = Pattern.compile("[\\t ]*" + varName + "\\s*=\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Double.parseDouble(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        // TODO: Your code goes here

        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=\\s*\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)");
        Matcher m = p.matcher(fileContent);
        m.find();
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        return new Point(x, y);
    } 

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();
        // TODO: Your code goes here

        // Regular expression pattern to match train line name and stations
        String[] fileContent1 = fileContent.split("train_line_name\\s*=\\s*");
        List<List<String>> trainList = new ArrayList<>();
        for (String str:fileContent1) {
            if (str.contains("train_line_stations")) {
                String[] tempStrings = str.split("\n\\s*train_line_stations\\s*=\\s*");
                trainList.add(Arrays.asList(tempStrings));
            }
        }

        for (int i = 0; i < trainList.size(); i++) {
            String trainLineName = trainList.get(i).get(0).trim().substring(1,trainList.get(i).get(0).trim().length() - 1);
            String[] stationPairs = trainList.get(i).get(1).split("\\)");
            List<String> stationList = new ArrayList<>(Arrays.asList(stationPairs));

            List<String> tempList = new ArrayList<>();
            for (String pair : stationList) {
                if (pair.matches(".*\\d.*")) {
                    tempList.add(pair);
                }
            }
            stationList = tempList;
            List<Station> stations = new ArrayList<>();

            int j = 1;
            for (String pair : stationList) {
                String pairWithoutParentheses = pair.replace("(", "").trim();
                String[] coordinates = pairWithoutParentheses.split("\\s*,\\s*");

                int x = Integer.parseInt(coordinates[0].trim());
                int y = Integer.parseInt(coordinates[1].trim());
                stations.add(new Station(new Point(x, y), trainLineName.substring(0, trainLineName.length()) + " Line Station " + j));
                j++;
            }
            trainLines.add(new TrainLine(trainLineName, stations));
        }
        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {

        // TODO: Your code goes here

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n"); // Her satırı StringBuilder'a ekliyoruz
            }
            String fileContent = sb.toString();

            numTrainLines = getIntVar("num_train_lines", fileContent);
            startPoint = new Station(getPointVar("starting_point", fileContent),"Starting Point");
            destinationPoint = new Station(getPointVar("destination_point", fileContent),"Final Destination");
            averageTrainSpeed = getDoubleVar("average_train_speed", fileContent) / 0.06;
            lines = getTrainLines(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}