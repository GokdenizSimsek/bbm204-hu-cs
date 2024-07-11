public class Merge {
    public static int[] mergeSort(int[] A) {
        int n = A.length;
        if (n <= 1) {
            return A;
        }

        int mid = n / 2;
        int[] left = new int[mid];
        int[] right = new int[n - mid];

        // Split the array into two
        for (int i = 0; i < mid; i++) {
            left[i] = A[i];
        }
        for (int i = mid; i < n; i++) {
            right[i - mid] = A[i];
        }

        // Sort sorted subarrays
        left = mergeSort(left);
        right = mergeSort(right);

        // Combining
        return merge(left, right);
    }

    public static int[] merge(int[] A, int[] B) {
        int[] C = new int[A.length + B.length];
        int i = 0, j = 0, k = 0;

        // As long as both subarrays have elements
        while (i < A.length && j < B.length) {
            if (A[i] > B[j]) {
                C[k++] = B[j++];
            } else {
                C[k++] = A[i++];
            }
        }

        while (i < A.length) {
            C[k++] = A[i++];
        }

        while (j < B.length) {
            C[k++] = B[j++];
        }

        return C;
    }
}