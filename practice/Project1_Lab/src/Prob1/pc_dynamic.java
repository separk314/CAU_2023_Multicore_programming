package Prob1;

public class pc_dynamic {

    private static int NUM_END = 200000; // default input
    private static int NUM_THREADS = 4; // default number of threads
    private static int TASK_SIZE = 10;  // default task size

    public static void main(String[] args) {
        if (args.length == 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }

        int totalCounter = 0;
        DynamicThread[] threads = new DynamicThread[NUM_THREADS];
        IndexGenerator indexGenerator = new IndexGenerator();   // threads share the indexGenerator

        long startTime = System.currentTimeMillis();    // program execution time starts

        // Start threads
        for(int i=0; i<NUM_THREADS; i++) {
            threads[i] = new DynamicThread(indexGenerator, NUM_END, NUM_THREADS);
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

class DynamicThread extends Thread {
    private static int TASK_SIZE = 10;  // default task size

    int NUM_END, NUM_THREADS, counter, num;
    long timeDiff;
    IndexGenerator indexGenerator;

    DynamicThread(IndexGenerator indexGenerator, int NUM_END, int NUM_THREADS) {
        this.indexGenerator = indexGenerator;
        this.NUM_END = NUM_END;
        this.NUM_THREADS = NUM_THREADS;
        this.counter = 0;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        num = indexGenerator.generateIndex();   // IndexGenerator gives the number to calculate

        while (num <= NUM_END) {
            for(int i=num; i<num+10; i++) {
                if(isPrime(i))    counter++;
            }
            num = indexGenerator.generateIndex();
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

class IndexGenerator {
    public static int index = 0;

    /*
        IndexGenerator stores the last number the threads have calculated.
        Threads can get the number by 'generateIndex()'
        'synchronized' keyword is used to make the function a critical section
        because 'index' variable should be protected when one thread is accessing 'index'
     */
    public synchronized int generateIndex() {
        index += 10;
        return index;
    }
}