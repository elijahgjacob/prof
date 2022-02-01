/** Multidimensional array
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
     * {{"hello","you","world"} ,{"how","are","you"}} prints:
     * Rows: 2
     * Columns: 3
     * <p>
     * {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
     * Rows: 4
     * Columns: 4
     */
    public static void printRowAndCol(int[][] arr) {
        for (int row = 0; row < arr.length; row++) {
            for (int column = 0; column < arr[row].length; column++) {
                System.out.println(arr[row][column]);
            }
        }
    }

    /**
     * @param arr: 2d array
     * @return maximal value present anywhere in the 2d array
     */
    public static int maxValue(int[][] arr) {
        int max = -999;
        for (int row = 0; row < arr.length; row++) {
            for (int column = 0; column < arr[row].length; column++) {
                if (arr[row][column] > max) {
                    max = arr[row][column];
                }
            }
        }
        return max;
    }

    /**
     * Return an array where each element is the sum of the
     * corresponding row of the 2d array
     */
    public static int[] allRowSums(int[][] arr) {
        int[] sum;
        sum = new int[arr.length];
        for (int row = 0; row < arr.length; row++) {
            for (int column = 0; column < arr[row].length; column++) {
                sum[row] += arr[row][column];
            }
        }
        return sum;
    }
}


