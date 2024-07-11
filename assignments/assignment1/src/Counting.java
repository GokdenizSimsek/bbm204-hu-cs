public class Counting{
    public static int[] countingSort(int[] A, int k) {
        int[] count = new int[k + 1]; // Count array with size k + 1
        int[] output = new int[A.length]; // Output array with the same length as A

        // Count the occurrences of each element in A
        for (int i = 0; i < A.length; i++) {
            count[A[i]]++;
        }

        // Update the count array to store the actual position of each element in the sorted array
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Build the output array
        for (int i = A.length - 1; i >= 0; i--) {
            output[count[A[i]] - 1] = A[i];
            count[A[i]]--;
        }

        return output;
    }
}