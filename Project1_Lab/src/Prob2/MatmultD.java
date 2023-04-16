package Prob2;
import java.util.*;
import java.lang.*;

/*
    < command-line execution >
    Ex) java MatmultD 6 < mat500.txt
    6 means the number of threads to use
    < mat500.txt means the file that contains two matrices is given as standard input
 */

public class MatmultD
{
    private static int NUM_THREADS = 1;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
        if (args.length == 1)
            NUM_THREADS = Integer.valueOf(args[0]);

        int a[][] = readMatrix();
        int b[][] = readMatrix();

        MulMatrixThread[] threads = new MulMatrixThread[NUM_THREADS];

        long startTime = System.currentTimeMillis();    // program execution time starts

//        int[][] c = multMatrix(a,b);

        // Start threads
        for (int i=0; i<NUM_THREADS; i++) {
            threads[i] = new MulMatrixThread();
            threads[i].start();
        }

        // Thread join()
        try {
            for (int i=0; i<NUM_THREADS; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {}

        long endTime = System.currentTimeMillis();  // program execution time ends

//        printMatrix(c);

        // Print Result
        System.out.printf("[thread_no]:%2d , [Total Execution Time]:%4d ms\n", NUM_THREADS, endTime-startTime);
        for (int i=0; i<NUM_THREADS; i++) {
            System.out.println(i+1 + " Thread: " + threads[i].diffTime + "ms");
        }
    }

    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    public static void printMatrix(int[][] mat) {
        System.out.println("Matrix["+mat.length+"]["+mat[0].length+"]");
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%4d " , mat[i][j]);
                sum+=mat[i][j];
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }

    public static int[][] multMatrix(int a[][], int b[][]) {     // a[m][n], b[n][p]
        if(a.length == 0) return new int[0][0];
        if(a[0].length != b.length) return null; // invalid dims

        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        int ans[][] = new int[m][p];

        for(int i = 0;i < m;i++){
            for(int j = 0;j < p;j++){
                for(int k = 0;k < n;k++){
                    ans[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return ans;
    }
}

class MulMatrixThread extends Thread {
    long diffTime;

    MulMatrixThread() {

    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        

        long endTime = System.currentTimeMillis();
        diffTime = endTime - startTime;
    }
}