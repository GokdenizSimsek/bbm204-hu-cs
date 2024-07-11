import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Main {
    public static void main(String args[]) throws IOException {
        // get datas from input file
        List<Integer> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitted = line.split(",");
                data.add(Integer.parseInt(splitted[6]));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // save data to array from list
        int[] dataArray = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i] = data.get(i);
        }
        // X axis data
        int[] dataSizes = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        // Create sorted and reverse sorted arrays
        int[] sortedDataArray = new int[data.size()];
        sortedDataArray = Merge.mergeSort(dataArray);
        int[] reverseSortedDataArray = new int[data.size()];
        reverseSortedDataArray = Arrays.copyOf(sortedDataArray, sortedDataArray.length);

        for (int i = 0; i < reverseSortedDataArray.length / 2; i++) {
            int temp = reverseSortedDataArray[i];
            reverseSortedDataArray[i] = reverseSortedDataArray[reverseSortedDataArray.length - 1 - i];
            reverseSortedDataArray[reverseSortedDataArray.length - 1 - i] = temp;
        }

        // Create sample data for linear runtime
        double[][] yAxis = new double[3][10];
        int[] data1;
        int[] data2;
        int[] data3;

        for (int i = 0; i < dataSizes.length; i++) {

            long insertionTime = 0;
            long mergeTime = 0;
            long countingTime= 0;

            data1 = Arrays.copyOfRange(dataArray, 0, dataSizes[i]);
            data2 = Arrays.copyOfRange(dataArray, 0, dataSizes[i]);
            data3 = Arrays.copyOfRange(dataArray, 0, dataSizes[i]);

            for (int x = 0; x < 10; x++) {
                long insertionStartTime = System.currentTimeMillis();
                Insertion.insertionSort(data1);
                long insertionFinishTime = System.currentTimeMillis();
                insertionTime += (insertionFinishTime - insertionStartTime);

                long mergeStartTime = System.currentTimeMillis();
                Merge.mergeSort(data2);
                long mergeFinishTime = System.currentTimeMillis();
                mergeTime += (mergeFinishTime - mergeStartTime);

                long countingStartTime = System.currentTimeMillis();
                int maxNumber = findMaxNumber(data3);
                Counting.countingSort(data3, maxNumber);
                long countingFinishTime = System.currentTimeMillis();
                countingTime += (countingFinishTime - countingStartTime);
            }
            yAxis[0][i] = insertionTime / 10;
            yAxis[1][i] = mergeTime / 10;
            yAxis[2][i] = countingTime / 10;
        }

        // Save the char as .png and show it
        showAndSaveChart("Random Data Sorting", dataSizes, yAxis, "Insertion Sort", "Merge Sort", "Counting Sort", "Time in Milliseconds");

        double[][] sortedYAxis = new double[3][10];
        int[] sortedData1;
        int[] sortedData2;
        int[] sortedData3;

        for (int i = 0; i < dataSizes.length; i++) {

            long insertionTime = 0;
            long mergeTime = 0;
            long countingTime= 0;

            sortedData1 = Arrays.copyOfRange(sortedDataArray, 0, dataSizes[i]);
            sortedData2 = Arrays.copyOfRange(sortedDataArray, 0, dataSizes[i]);
            sortedData3 = Arrays.copyOfRange(sortedDataArray, 0, dataSizes[i]);

            for (int x = 0; x < 10; x++) {
                long insertionStartTime = System.currentTimeMillis();
                Insertion.insertionSort(sortedData1);
                long insertionFinishTime = System.currentTimeMillis();
                insertionTime += (insertionFinishTime - insertionStartTime);

                long mergeStartTime = System.currentTimeMillis();
                Merge.mergeSort(sortedData2);
                long mergeFinishTime = System.currentTimeMillis();
                mergeTime += (mergeFinishTime - mergeStartTime);

                long countingStartTime = System.currentTimeMillis();
                int maxNumber = findMaxNumber(sortedData3);
                Counting.countingSort(sortedData3, maxNumber);
                long countingFinishTime = System.currentTimeMillis();
                countingTime += (countingFinishTime - countingStartTime);
            }
            sortedYAxis[0][i] = insertionTime / 10;
            sortedYAxis[1][i] = mergeTime / 10;
            sortedYAxis[2][i] = countingTime / 10;
        }

        // Save the char as .png and show it
        showAndSaveChart("Sorted Data Sorting", dataSizes, sortedYAxis, "Insertion Sort", "Merge Sort", "Counting Sort", "Time in Milliseconds");

        double[][] reverseSortedYAxis = new double[3][10];
        int[] reverseSortedData1;
        int[] reverseSortedData2;
        int[] reverseSortedData3;

        for (int i = 0; i < dataSizes.length; i++) {

            long insertionTime = 0;
            long mergeTime = 0;
            long countingTime= 0;

            reverseSortedData1 = Arrays.copyOfRange(reverseSortedDataArray, 0, dataSizes[i]);
            reverseSortedData2 = Arrays.copyOfRange(reverseSortedDataArray, 0, dataSizes[i]);
            reverseSortedData3 = Arrays.copyOfRange(reverseSortedDataArray, 0, dataSizes[i]);

            for (int x = 0; x < 10; x++) {
                long insertionStartTime = System.currentTimeMillis();
                Insertion.insertionSort(reverseSortedData1);
                long insertionFinishTime = System.currentTimeMillis();
                insertionTime += (insertionFinishTime - insertionStartTime);

                long mergeStartTime = System.currentTimeMillis();
                Merge.mergeSort(reverseSortedData2);
                long mergeFinishTime = System.currentTimeMillis();
                mergeTime += (mergeFinishTime - mergeStartTime);

                long countingStartTime = System.currentTimeMillis();
                int maxNumber = findMaxNumber(reverseSortedData3);
                Counting.countingSort(reverseSortedData3, maxNumber);
                long countingFinishTime = System.currentTimeMillis();
                countingTime += (countingFinishTime - countingStartTime);
            }
            reverseSortedYAxis[0][i] = insertionTime / 10;
            reverseSortedYAxis[1][i] = mergeTime / 10;
            reverseSortedYAxis[2][i] = countingTime / 10;
        }

        // Save the char as .png and show it
        showAndSaveChart("Reverse Sorted Data Sorting", dataSizes, reverseSortedYAxis, "Insertion Sort", "Merge Sort", "Counting Sort", "Time in Milliseconds");


        double[][] searchingYAxis = new double[3][10];
        int[] searchingData1;
        int[] searchingData2;
        int[] searchingData3;

        for (int i = 0; i < dataSizes.length; i++) {

            long randomLinearSearchingTime = 0;
            long sortedLinearSearchingTime = 0;
            long sortedBinarySearchingTime = 0;

            searchingData1 = Arrays.copyOfRange(dataArray, 0, dataSizes[i]);
            searchingData2 = Arrays.copyOfRange(sortedDataArray, 0, dataSizes[i]);
            searchingData3 = Arrays.copyOfRange(sortedDataArray, 0, dataSizes[i]);

            for (int x = 0; x < 1000; x++) {
                Random rand = new Random();
                int randomIndex = rand.nextInt(dataSizes[i]);

                int randomValue = dataArray[randomIndex];
                int sortedValue = sortedDataArray[randomIndex];

                long randomLinearStartTime = System.nanoTime();
                LinearSearch.linearSearch(searchingData1, randomValue);
                long randomLinearFinishTime = System.nanoTime();
                randomLinearSearchingTime += (randomLinearFinishTime - randomLinearStartTime);

                long sortedLinearStartTime = System.nanoTime();
                LinearSearch.linearSearch(searchingData2, sortedValue);
                long sortedLinearFinishTime = System.nanoTime();
                sortedLinearSearchingTime += (sortedLinearFinishTime - sortedLinearStartTime);

                long sortedBinaryStartTime = System.nanoTime();
                BinarySearch.binarySearch(searchingData3, sortedValue);
                long sortedBinaryFinishTime = System.nanoTime();
                sortedBinarySearchingTime += (sortedBinaryFinishTime - sortedBinaryStartTime);
            }
            searchingYAxis[0][i] = randomLinearSearchingTime / 1000;
            searchingYAxis[1][i] = sortedLinearSearchingTime / 1000;
            searchingYAxis[2][i] = sortedBinarySearchingTime / 1000;
        }

        // Save the char as .png and show it
        showAndSaveChart("Searching Algorithms", dataSizes, searchingYAxis, "Random Linear Search", "Sorted Linear Search", "Sorted Binary Search", "Time in Nanoseconds");
    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis, String seriesName1, String seriesName2, String seriesName3, String timeType) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle(timeType).xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries(seriesName1, doubleX, yAxis[0]);
        chart.addSeries(seriesName2, doubleX, yAxis[1]);
        chart.addSeries(seriesName3, doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }

    private static int findMaxNumber(int[] arr) {
        int maxIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i;
            }
        }
        return arr[maxIndex];
    }
}