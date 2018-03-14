/**
 *
 * @author Amir Arbel
 *
 * Lesson 5 in class examples.
 *
 * Searching & sorting.
 */
public class Lesson5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] arr = {1,5,7,83,2,64,23,634, 0, 3, 0, 0, 0,5325, 352, 53,754};
        if (!isSorted(arr)) {
            printArray(arr);
            System.out.println("Sorting array..");
            selectionSort(arr);
        }
        printArray(arr);
    }

    public static void printArray(int[] haystack) {
        for (int index = 0; index < haystack.length; index = index +1) {
            System.out.print(haystack[index] + ", ");
        }
    }

    /**
     * Check if an array is sorted.
     *
     * @param haystack
     *  Array to check.
     * @return
     *   Whether the array is sorted.
     */
    public static boolean isSorted(int[] haystack) {
        for (int index = 0; index < haystack.length - 1; index = index + 1) {
            if (haystack[index] > haystack[index + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sort an array by selection sort.
     *
     * @param haystack
     */
    public static void  selectionSort(int[] haystack) {
        int index = 0;

        while (index < haystack.length - 1) {
            int minInd = minIndex(haystack, index);
            swap(haystack, index, minInd);
            System.out.println();
            System.out.print(index + ". - ");
            printArray(haystack);
            index = index + 1;
        }
    }

    /**
     * Switch two values in an array.
     * LESSON 4
     */
    public static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;

        return;
    }

    public static int  minIndex(int[] haystack, int index) {
        // Didn't do anything in class.
        for (int i = 0; i < index; i = i + 1) {
            if (haystack[i] > haystack[index]) {
                return i;
            }
        }
        return index;
    }

    /**
     * Sort an array by insertion sort.
     */
    public static void insertionSort(int[] haystack) {
        int index = 1;

        while (index < haystack.length) {
            // Insert the current number to the right place.
            insert(haystack, index);
           index = index + 1;
        }
    }

    /**
     * Helper function for insertionSort().
     *
     * @param haystack
     * @param index
     */
    public static void insert(int[] haystack, int index) {
        int insertedNumber = haystack[index];
        while (index > 0 && haystack[index - 1] > insertedNumber) {
            haystack[index] = haystack[index - 1];
            index = index - 1;
        }
        haystack[index] = insertedNumber;
    }

    /**
     * Sort an array incrementally.
     * Recursive function (not by Kodish)
     *
     * @param haystack
     *   Array to sort.
     */
    public static void  linearSort(int[] haystack) {
        for (int index = 0; index < haystack.length - 1; index = index + 1) {
            if (haystack[index] > haystack[index + 1]) {
                // Switch the two variables, so that the larger one is later in
                // the array.
                int temp = haystack[index + 1];
                haystack[index + 1] = haystack[index];
                haystack[index] = temp;
                linearSort(haystack);
            }
        }
    }


    /**
     * Find an integer in an array using binary search.
     *
     * @param needle
     *   Integer to search for.
     * @param haystack
     *   Array to search in
     *
     * @return
     */
    public static int binarySearch(int[] haystack, int needle) {
        int low = 0, high = haystack.length - 1;

        while (low <= high) {
            int index = (low + high) / 2;
            if (haystack[index] == needle) {
                return index;
            }
            else if (haystack[index] > needle) {
                high = index - 1;
            }
            else {
                low = index + 1;
            }
        }
        return -1;
    }

    /**
     * Find an integer in an array.
     *
     * @param needle
     *   Integer to search for.
     * @param haystack
     *   Array to search in
     *
     * @return
     */
    public static int linearSearch(int[] haystack, int needle) {
        // Iterate over the array looking for the needle.
        for (int index = 0; index < haystack.length; index = index + 1) {
            if (needle == haystack[index]) {
                return index;
            }
        }
        return -1;
    }
}
