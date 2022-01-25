/** Solutions to the HW0 Java101 exercises.
 *  @author Allyson Park and [INSERT YOUR NAME HERE]
 */
public class Solutions {
    public static boolean isEven(int x) {
        return (x % 2 == 0);
    }

    public static int max (int[] a) {
        int maxno = a[0];
        for (int i =1; i <a.length; i++ ){
            if(a[i]> maxno){
                maxno=a[i];
            }
        }
        return 0;
    }

    public static boolean wordBank(String word, String []bank){
        for (int i =0; i<bank.length; i ++){
            if(bank[i].equals(word)){
                return true;
            }
        }

        return false;
    }
    public static boolean threeSum(int[] a){
        for (int i =0; i<a.length; i++){
            for (int j =0; j<a.length; j++){
                for (int k =0; k<a.length; k++){
                    if (a[i] + a[j] + a[k] ==0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
