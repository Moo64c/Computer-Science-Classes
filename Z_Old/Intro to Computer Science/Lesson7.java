/**
 *
 * @author Amir Arbel
 *
 * Classes.
 */
public class Lesson7 {
    public static void main(String args[]) {
        Lesson3 l3 = new Lesson3(2);

    }

    public static void subsets(int n, int k) {
        subsets(n, k, "", 0);
    }

    public static void subsets(int n, int k, String s, int max) {
        if (s.length() == k)
            System.out.println(s);
        else for(int i = max + 1; i <= n; i++) {
            subsets(n,k, s + i, i);
        }
    }
}


