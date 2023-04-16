public class pc_static_block {
    private static int NUM_END = 200000; // default input
    private static int NUM_THREADS = 1; // default number of threads

    public static void main(String[] args) {
        if (args.length == 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }

        int blockSize = (int) Math.ceil(NUM_END / NUM_THREADS);
        int totalCounter = 0;
        BlockThread[] threads = new BlockThread[NUM_THREADS];

        long startTime = System.currentTimeMillis();    // program execution time starts

        // start threads
        for(int i=0; i<NUM_THREADS; i++) {
            int end;
            if (i == NUM_THREADS-1) {
                end = NUM_END;  // if thread[i] is last thread: end number is NUM_END
            } else {
                end = i*blockSize + blockSize;  // if thread[i] is not last thread: calculate by blockSize
            }

            threads[i] = new BlockThread(i*blockSize, end);
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

class BlockThread extends Thread {
    int start, end, counter;
    long timeDiff;

    /*
        BlockThread tests whether the number is a prime number or not
        from 'start' number ~ to 'end' number
     */

    BlockThread(int start, int end) {
        this.start = start;
        this.end = end;
        this.counter = 0;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for (int num=start; num<end; num++) {
            if(isPrime(num))    counter++;
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