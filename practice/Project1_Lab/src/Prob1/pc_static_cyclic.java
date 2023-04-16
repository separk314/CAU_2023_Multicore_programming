package Prob1;

public class pc_static_cyclic {
    private static int NUM_END = 200000; // default input
    private static int NUM_THREADS = 1; // default number of threads
    private static int TASK_SIZE = 10;  // default task size

    public static void main(String[] args) {
        if (args.length == 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }

        int totalCounter = 0;
        CyclicThread[] threads = new CyclicThread[NUM_THREADS];

        long startTime = System.currentTimeMillis();    // program execution time starts

        // Run threads
        for(int i=0; i<NUM_THREADS; i++) {
            threads[i] = new CyclicThread(i, NUM_END, NUM_THREADS);
            threads[i].start();
        }

        // Thread join()
        for (int i=0; i<NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }

        // Get the total number of prime numbers
        for (int i=0; i<NUM_THREADS; i++) {
            totalCounter += threads[i].counter;
        }

        long endTime = System.currentTimeMillis();      // program execution time ends
        long timeDiff = endTime - startTime;

        // print the result
        System.out.println("\n          < RESULT > ");
        for (int i=0; i<NUM_THREADS; i++) {
            System.out.println(i +" Thread: " + threads[i].timeDiff + " ms");
        }
        System.out.println("\nTotal Program Execution Time: " + timeDiff + "ms");
        System.out.println("1... " + (NUM_END - 1) + " prime# counter=" + totalCounter + "\n");
    }
}

class CyclicThread extends Thread {
    private static int TASK_SIZE = 10;  // default task size

    int threadIndex, NUM_END, NUM_THREADS, counter;
    long timeDiff;

    /*
        A CyclicThread get the number to calculate by its 'threadIndex'
        if 'threadIndex' == 0 and NUM_THREADS == 4:
            it starts calculating from 0 to 9,
            the next number to calculate is (0 + TASK_SIZE*NUM_THREADS),
            therefore calculates 40 ~ 49,
            and then 80 ~ 89 ... and so on.
     */

    CyclicThread(int threadIndex, int NUM_END, int NUM_THREADS) {
        this.threadIndex = threadIndex;
        this.NUM_END = NUM_END;
        this.NUM_THREADS = NUM_THREADS;
        this.counter = 0;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        /*
            'num' is a starting number to calculate.
            If num is 0, start calculating from 0,
            If num is 3, start calculating from 30...
            (because the TASK_SIZE is 10)
         */
        int num = threadIndex * TASK_SIZE;
        while (num <= NUM_END) {
            for(int i=num; i<num+TASK_SIZE; i++) {
                if(isPrime(i))    counter++;
            }

            num += NUM_THREADS * TASK_SIZE;
        }

        long endTime = System.currentTimeMillis();
        timeDiff = endTime - startTime;
    } 

    private static boolean isPrime(int x) {
        if (x<=1)    return false;
        for(int i=2; i<x; i++) {
            if (x%i == 0)   return false;
        }
        return true;
    }
}