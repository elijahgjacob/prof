import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k ; ++i) {
                int temp = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > temp) {
                    array[j + 1] = array[j];
                    j = j - 1;
                }
                array[j + 1] = temp;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int j;
            for (int i = 0; i < k-1; i++) {
                int min = i;
                for (j = i + 1; j < k; j++) {
                    if (array[j] <= array[min]) {
                        min = j;
                    }
                }
                int temp = array[i];
                array[i] = array[min];
                array[min] = temp;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        public static void merge(int[] array, int high, int low, int mid) {
            int k = array.length;
            high = Integer.MIN_VALUE;
            low = Integer.MAX_VALUE;
            mid = Math.floorDiv(array.length, 2);
            for (int i = 0; i < array.length; i++) {
                if (array[i] > high) {
                    high = i;
                }
            }
            for (int i = 0; i < array.length; i++) {
                if (array[i] < low) {
                    low = i;
                }
            }
            int leftArr[] = new int[mid - low + 1];
            int rightArr[] = new int[high - mid];

            for (int i = 0; i < leftArr.length; i++) {
                leftArr[i] = array[low + i];
            }
            for (int i = 0; i < rightArr.length; i++) {
                rightArr[i] = array[mid + i + 1];
            }

            int left = 0;
            int right = 0;

            for (int i = low; i < high + 1; i++) {
                if (left < leftArr.length && right < rightArr.length) {
                    if (leftArr[left] < rightArr[right]) {
                        array[i] = leftArr[left];
                        left++;
                    } else {
                        array[i] = rightArr[right];
                        right++;
                    }
                } else if (left < leftArr.length) {
                    array[i] = leftArr[left];
                    left++;
                } else if (right < rightArr.length) {
                    array[i] = rightArr[right];
                    right++;
                }
            }

        }
        void sort(int[] array, int high, int low) {
            if (high <= low) {
                int mid = (low + high) / 2;
                sort(array, low, mid);
                sort(array, mid + 1, high);
                merge(array, low, mid, high);
            }
        }
        @Override
        public String toString () {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
//took out an @Override
        static int getMax (int [] arr, int k){ //finds the smallest item in the array
            int max = arr[0];
            for (int i = 1; i < k; i++) {
                if (arr[i] > max)
                    max = arr[i];
            }
            return max;
        }

        static void countSort(int arr[], int k, int exp) {
            int output[] = new int[k]; // output array
            int count[] = new int[10]; //digit array
            for (int i =0; i < 10; i++){ //sets all elements of the count array to 0
                count [i] = 0;
            }

            for (int i = 0; i < k; i++){ //frequency of digits are calculated
                int z = arr[i]/exp % 10;
                count [z] += 1 ; //populates
            }

            for (int i = 1; i < 10; i++){ //sort
                count [i] += count [i - 1];
            }

            for (int i = k - 1; i >= 0; i--) { //Elements are placed into the array in order
                int z = arr[i]/exp % 10;
                output[count[z] - 1] = arr[i];
                count[z] -= 1;
            }

            for (int i = 0; i < k; i++){
                arr[i] = output[i];
            }

        }
        public void sort(int[] arr, int k) {
            int maxdig = getMax(arr, k); // Find the maximum number to know the number of digits
            for (int exp = 1; maxdig / exp > 0; exp *= 10) { //counting sort for every digit
                countSort(arr, k, exp);
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
