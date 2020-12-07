import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1, 2, 5, 16, -1, -2, 0, 32, 3, 5, 8, 23, 4);
        List<Integer> nIntList = new ArrayList<>();
        for (Integer i : intList) {
            if ((i % 2 == 0) && (i > 0)) {
                nIntList.add(i);
            }
        }
        Integer[] array = nIntList.toArray(new Integer[0]);
        quickSort(array, 0, array.length - 1);
        for (Integer a : array) {
            System.out.println(a);
        }
    }

    public static void quickSort(Integer[] A, int first, int last) {
        int i = first;
        int j = last;
        int x = A[first + (last - first) / 2];
        do {
            while (A[i] < x) i++;
            while (A[j] > x) j--;
            if (i <= j) {
                int temp = A[i];
                A[i] = A[j];
                A[j] = temp;
                i++;
                j--;
            }
        }
        while (i <= j);
        if (first < j) quickSort(A, first, j);
        if (i < last) quickSort(A, i, last);
    }
}
